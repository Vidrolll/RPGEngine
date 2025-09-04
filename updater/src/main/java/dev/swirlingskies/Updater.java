package dev.swirlingskies;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.KeyFactory;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.zip.GZIPInputStream;

public class Updater {
    private static final String CURRENT_VERSION_FILE = "current.version";

    private static final OkHttpClient http = new OkHttpClient();

    public static void main(String[] args) throws Exception {
        if(args.length==0) throw new Exception("Must supply a manifest URL");
        String manifestUrl = args[0];
        Path installDir = (args.length >= 2) ? Paths.get(args[1]) : detectInstallDir();
        Files.createDirectories(installDir);

        run(manifestUrl, installDir);
    }

    private static void run(String manifestUrl, Path installDir) throws Exception {
        String current = readCurrentVersion(installDir);
        Manifest remote = fetchManifest(manifestUrl);

        if(!isNewer(remote.version, current)) {
            System.out.println("Up-to-date. Launching existing version.");
            launch(remote, installDir);
            return;
        }

        Path tmp = Files.createTempFile("update-", remote.archiveFileName());
        try {
            download(remote.url, tmp);
            verifySha256(tmp, remote.sha256);
            Path targetDir = installDir.resolve(remote.version);
            extractArchive(tmp, targetDir);
            writeCurrentVersion(installDir, remote.version);
            System.out.println("Updated to " + remote.version);
        } finally {
            try {
                Files.deleteIfExists(tmp);
            } catch(IOException ignore) {}
        }
        launch(remote, installDir);
    }

    private static void launch(Manifest m, Path installDir) throws IOException {
        List<String> cmd = new ArrayList<>();
        if(m.launchCommand != null && !m.launchCommand.isEmpty()) {
            cmd.addAll(m.launchCommand);
        } else {
            String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
            cmd.add(javaBin);
            cmd.add("-jar");
            try(DirectoryStream<Path> ds = Files.newDirectoryStream(installDir, "launcher-*.jar")) {
                for(Path p : ds) {
                    cmd.add(p.toAbsolutePath().toString());
                    break;
                }
            }
        }
        System.out.println("Launching: " + cmd);
        new ProcessBuilder(cmd).directory(installDir.toFile()).inheritIO().start();
    }

    private static Path detectInstallDir() throws Exception {
        Path jar = Paths.get(Updater.class.getProtectionDomain().
                getCodeSource().getLocation().toURI());
        return (Files.isRegularFile(jar) ? jar.getParent() :
                jar).toAbsolutePath().normalize();
    }

    private static String readCurrentVersion(Path dir) {
        Path f = dir.resolve(CURRENT_VERSION_FILE);
        if(!Files.exists(f)) return "0.0.0";
        try {
            return Files.readString(f, StandardCharsets.UTF_8).trim();
        } catch(IOException e) {
            return "0.0.0";
        }
    }

    private static boolean isNewer(String a, String b) {
        try {
            int[] A = parseVer(a), B = parseVer(b);
            for(int i = 0; i < Math.max(A.length, B.length); i++) {
                int ai = i < A.length ? A[i] : 0;
                int bi = i < B.length ? B[i] : 0;
                if(ai != bi) return ai > bi;
            }
            return false;
        } catch(Exception e) {
            return a.compareTo(b) > 0;
        }
    }

    private static int[] parseVer(String v) {
        String[] parts = v.replaceAll("[^0-9.]", "").split("\\.");
        int[] out = new int[parts.length];
        for (int i = 0; i < parts.length; i++)
            out[i] = parts[i].isEmpty() ? 0 : Integer.parseInt(parts[i]);
        return out;
    }

    private static void download(String url, Path dest) throws IOException {
        Request req = new Request.Builder().url(url).build();
        try(Response resp = http.newCall(req).execute()) {
            if(!resp.isSuccessful()) throw new IOException("HTTP " + resp.code() + " downloading");
            long total = Optional.ofNullable(resp.body().contentLength()).orElse(-1L);
            try(InputStream in = resp.body().byteStream();
                OutputStream out = Files.newOutputStream(dest, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                byte[] buf = new byte[8192];
                long read = 0, last = 0;
                for(int n; (n = in.read(buf)) > 0; ) {
                    out.write(buf, 0, n);
                    read += n;
                    if(total > 0 && read - last > 1000000) {
                        System.out.println("Progress: " + (int)(100 * read / total) + "%");
                        last = read;
                    }
                }
            }
        }
    }

    private static void extractArchive(Path archive, Path targetDir) throws IOException {
        String name = archive.getFileName().toString().toLowerCase(Locale.ROOT);
        Files.createDirectories(targetDir);
        if(name.endsWith(".zip")) {
            try(ZipFile zf = new ZipFile(archive.toFile())) {
                var en = zf.getEntries();
                while(en.hasMoreElements()) {
                    ZipArchiveEntry e = en.nextElement();
                    Path out = targetDir.resolve(e.getName()).normalize();
                    ensureInside(targetDir, out);
                    if(e.isDirectory()) Files.createDirectories(out);
                    else {
                        Files.createDirectories(out.getParent());
                        try(InputStream in = zf.getInputStream(e);
                            OutputStream os = Files.newOutputStream(out)) {
                            in.transferTo(os);
                        }
                    }
                }
            }
        } else if(name.endsWith(".tar.gz") || name.endsWith(".tgz")) {
            try(InputStream fi = Files.newInputStream(archive);
                    GZIPInputStream gzi = new GZIPInputStream(fi);
                    TarArchiveInputStream ti = new TarArchiveInputStream(gzi)) {
                TarArchiveEntry e;
                while((e = ti.getNextEntry()) != null) {
                    Path out = targetDir.resolve(e.getName()).normalize();
                    ensureInside(targetDir, out);
                    if(e.isDirectory()) Files.createDirectories(out);
                    else {
                        Files.createDirectories(out.getParent());
                        try(OutputStream os = Files.newOutputStream(out)) {
                            ti.transferTo(os);
                        }
                    }
                }
            }
        } else {
            Files.copy(archive, targetDir.resolve(name), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private static void ensureInside(Path root, Path out) throws IOException {
        if(!out.normalize().startsWith(root.normalize())) throw new IOException("Blocked path traversal: " + out);
    }

    private static void writeCurrentVersion(Path dir, String v) throws IOException {
        Path tmp = dir.resolve(CURRENT_VERSION_FILE + ".tmp");
        Files.writeString(tmp, v + "\n", StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        Files.move(tmp, dir.resolve(CURRENT_VERSION_FILE),
                StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
    }

    private static final ObjectMapper json = new ObjectMapper().configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
    private static final String MANIFEST_PUBKEY_B64 = "MCowBQYDK2VwAyEAHRnugxXxycxz+oG4d5CF2RoNFpQ5w0HsElflMkkUX6o=";

    private static void verifySha256(Path file, String expectedHex) throws IOException {
        if(expectedHex == null || expectedHex.isBlank()) return;
        try(InputStream in = Files.newInputStream(file)) {
            String actual = DigestUtils.sha256Hex(in);
            if(!actual.equalsIgnoreCase(expectedHex)) {
                throw new IOException("Checksum mismatch: expected=" + expectedHex + ", actual=" + actual);
            }
            System.out.println("Checksum OK");
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static final class Manifest {
        public String version;
        public String url;
        public String sha256;
        public List<String> launchCommand;
        public String archiveFileName() {
            if(url == null || url.isBlank()) return "update.bin";
            try {
                return Paths.get(new URI(url).getPath()).getFileName().toString();
            } catch(Exception e) {
                return url.substring(url.lastIndexOf("/") + 1);
            }
        }
    }

    private static Manifest fetchManifest(String url) throws IOException {
        byte[] raw;
        try(Response r = http.newCall(new Request.Builder().url(url).build()).execute()) {
            if(!r.isSuccessful()) throw new IOException("HTTP" + r.code() + " fetching manifest");
            raw = r.body().bytes();
        }
        JsonNode tree = json.readTree(raw);
        if(tree == null) throw new IOException("Empty manifest");

        if(MANIFEST_PUBKEY_B64 != null) {
            JsonNode sigNode = tree.get("_sig");
            if(sigNode == null) throw new IOException("Manifest missing _sig");
            String sigB64 = sigNode.asText("").trim();
            ((ObjectNode)tree).remove("_sig");
            byte[] canonical = json.writer().writeValueAsBytes(tree);
            if(!verifyEd25519(canonical, Base64.getDecoder().decode(sigB64))) {
                throw new IOException("Manifest signature verification failed");
            }
        }
        return json.treeToValue(tree, Manifest.class);
    }

    private static boolean verifyEd25519(byte[] data, byte[] sig) {
        try {
            byte[] der = Base64.getDecoder().decode(MANIFEST_PUBKEY_B64);
            var spec = new X509EncodedKeySpec(der);
            var pk = KeyFactory.getInstance("Ed25519").generatePublic(spec);
            var ver = Signature.getInstance("Ed25519");
            ver.initVerify(pk);
            ver.update(data);
            return ver.verify(sig);
        } catch(Exception e) {
            e.printStackTrace(System.err);
            return false;
        }
    }
}

