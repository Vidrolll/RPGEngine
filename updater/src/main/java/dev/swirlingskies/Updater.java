package dev.swirlingskies;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.PosixFilePermission;
import java.time.Duration;
import java.util.*;

/**
 * One-shot auto-updater.
 * Usage:
 *   java -jar updater.jar <manifestUrl> [installDir]
 *
 * Behaviour:
 *  - Reads remote JSON manifest (see UpdateManifest below)
 *  - Compares remote version vs current (from installDir/current.version)
 *  - If newer: downloads archive to temp, verifies sha256, extracts into installDir/<version>
 *  - Updates current.version, then launches the game/launcher (from manifest.launchCommand)
 */
public final class Updater {
    private static final Logger log = LoggerFactory.getLogger(Updater.class);

    private static final String CURRENT_VERSION_FILE = "current.version";

    private final OkHttpClient http = new OkHttpClient.Builder()
            .callTimeout(Duration.ofSeconds(60))
            .connectTimeout(Duration.ofSeconds(20))
            .readTimeout(Duration.ofSeconds(60))
            .build();

    private final ObjectMapper json = new ObjectMapper();

    public static void main(String[] args) {
        try {
            if (args.length < 1) {
                System.err.println("Usage: java -jar updater.jar <manifestUrl> [installDir]");
                System.exit(2);
            }
            String manifestUrl = args[0];
            Path installDir = (args.length >= 2)
                    ? Paths.get(args[1])
                    : detectInstallDir();

            new Updater().run(manifestUrl, installDir);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static Path detectInstallDir() throws IOException {
        // Directory containing the running JAR (updater)
        try {
            Path jar = Paths.get(Updater.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            Path dir = Files.isRegularFile(jar) ? jar.getParent() : jar; // runnable from classes folder too
            return dir.toAbsolutePath().normalize();
        } catch (Exception e) {
            throw new IOException("Unable to locate updater JAR directory", e);
        }
    }

    private void run(String manifestUrl, Path installDir) throws Exception {
        log.info("Install dir: {}", installDir);
        Files.createDirectories(installDir);

        UpdateManifest remote = fetchManifest(manifestUrl);
        String current = readCurrentVersion(installDir);
        log.info("Current version: {} | Remote version: {}", current, remote.version);

        if (!isNewer(remote.version, current)) {
            log.info("Up-to-date. Launching existing install.");
            launch(remote, installDir);
            return;
        }

        // Download
        Path tmp = Files.createTempFile("update-", remote.archiveFileName());
        try {
            download(remote.url, tmp);
            verifySha256(tmp, remote.sha256);

            // Extract
            Path targetDir = installDir.resolve(remote.version);
            extractArchive(tmp, targetDir);

            // Write current.version
            Files.writeString(installDir.resolve(CURRENT_VERSION_FILE), remote.version, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            log.info("Update applied: {}", targetDir);
        } finally {
            try { Files.deleteIfExists(tmp); } catch (IOException ignore) {}
        }

        launch(remote, installDir);
    }

    private UpdateManifest fetchManifest(String url) throws IOException {
        log.info("Fetching manifest: {}", url);
        Request req = new Request.Builder().url(url).build();
        try (Response resp = http.newCall(req).execute()) {
            if (!resp.isSuccessful()) throw new IOException("HTTP " + resp.code() + " getting manifest");
            try (InputStream is = Objects.requireNonNull(resp.body()).byteStream()) {
                return json.readValue(is, UpdateManifest.class);
            }
        }
    }

    private String readCurrentVersion(Path installDir) {
        Path f = installDir.resolve(CURRENT_VERSION_FILE);
        if (!Files.exists(f)) return "0.0.0"; // default
        try { return Files.readString(f, StandardCharsets.UTF_8).trim(); }
        catch (IOException e) { return "0.0.0"; }
    }

    private boolean isNewer(String a, String b) {
        // Basic semver-ish compare: split numeric/alpha. Fallback to string compare.
        try {
            int[] A = parseVersion(a); int[] B = parseVersion(b);
            for (int i = 0; i < Math.max(A.length, B.length); i++) {
                int ai = i < A.length ? A[i] : 0;
                int bi = i < B.length ? B[i] : 0;
                if (ai != bi) return ai > bi;
            }
            return false; // equal
        } catch (Exception e) {
            return a.compareTo(b) > 0;
        }
    }

    private int[] parseVersion(String v) {
        String[] parts = v.replaceAll("[^0-9.]", "").split("\\.");
        int[] out = new int[parts.length];
        for (int i = 0; i < parts.length; i++) out[i] = parts[i].isEmpty()?0:Integer.parseInt(parts[i]);
        return out;
    }

    private void download(String url, Path dest) throws IOException {
        log.info("Downloading {} -> {}", url, dest);
        Request req = new Request.Builder().url(url).build();
        try (Response resp = http.newCall(req).execute()) {
            if (!resp.isSuccessful()) throw new IOException("HTTP " + resp.code() + " downloading update");
            try (InputStream in = Objects.requireNonNull(resp.body()).byteStream();
                 OutputStream out = Files.newOutputStream(dest, StandardOpenOption.TRUNCATE_EXISTING)) {
                in.transferTo(out);
            }
        }
    }

    private void verifySha256(Path file, String expectedHex) throws IOException {
        if (expectedHex == null || expectedHex.isBlank()) { log.warn("No sha256 provided; skipping verify"); return; }
        try (InputStream in = Files.newInputStream(file)) {
            String actual = DigestUtils.sha256Hex(in);
            if (!actual.equalsIgnoreCase(expectedHex)) {
                throw new IOException("Checksum mismatch: expected=" + expectedHex + ", actual=" + actual);
            }
            log.info("Checksum OK");
        }
    }

    private void extractArchive(Path archive, Path targetDir) throws IOException {
        String name = archive.getFileName().toString().toLowerCase(Locale.ROOT);
        log.info("Extracting {} -> {}", name, targetDir);
        Files.createDirectories(targetDir);
        if (name.endsWith(".zip")) {
            try (ZipFile zf = new ZipFile(archive.toFile())) {
                Enumeration<ZipArchiveEntry> en = zf.getEntries();
                while (en.hasMoreElements()) {
                    ZipArchiveEntry e = en.nextElement();
                    Path out = targetDir.resolve(e.getName()).normalize();
                    ensureInside(targetDir, out);
                    if (e.isDirectory()) {
                        Files.createDirectories(out);
                    } else {
                        Files.createDirectories(out.getParent());
                        try (InputStream in = zf.getInputStream(e);
                             OutputStream os = Files.newOutputStream(out)) {
                            in.transferTo(os);
                        }
                    }
                }
            }
        } else if (name.endsWith(".tar.gz") || name.endsWith(".tgz")) {
            try (InputStream fi = Files.newInputStream(archive);
                 GzipCompressorInputStream gzi = new GzipCompressorInputStream(fi);
                 TarArchiveInputStream ti = new TarArchiveInputStream(gzi)) {
                TarArchiveEntry e;
                while ((e = ti.getNextTarEntry()) != null) {
                    Path out = targetDir.resolve(e.getName()).normalize();
                    ensureInside(targetDir, out);
                    if (e.isDirectory()) {
                        Files.createDirectories(out);
                    } else {
                        Files.createDirectories(out.getParent());
                        try (OutputStream os = Files.newOutputStream(out)) {
                            ti.transferTo(os);
                        }
                        Files.setPosixFilePermissions(out, EnumSet.of(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE, PosixFilePermission.OWNER_EXECUTE));
                    }
                }
            } catch (UnsupportedOperationException ignore) {
                // Windows may not support POSIX perms; ignore
            }
        } else {
            // plain file -> place into targetDir
            Path out = targetDir.resolve(name);
            Files.copy(archive, out, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private void ensureInside(Path root, Path out) throws IOException {
        if (!out.normalize().startsWith(root.normalize())) {
            throw new IOException("Blocked path traversal: " + out);
        }
    }

    private void launch(UpdateManifest m, Path installDir) throws IOException {
        List<String> cmd = new ArrayList<>();
        if (m.launchCommand != null && !m.launchCommand.isEmpty()) {
            cmd.addAll(m.launchCommand);
        } else {
            // default: java -jar launcher-<version>.jar
            String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
            cmd.add(javaBin);
            cmd.add("-jar");
            // try to find a launcher jar in installDir (best-effort)
            try (DirectoryStream<Path> ds = Files.newDirectoryStream(installDir, "launcher-*.jar")) {
                for (Path p : ds) { cmd.add(p.toAbsolutePath().toString()); break; }
            }
        }
        log.info("Launching: {}", cmd);
        new ProcessBuilder(cmd)
                .directory(installDir.toFile())
                .inheritIO()
                .start();
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
final class UpdateManifest {
    /** Version string, e.g., 1.2.3 */
    public String version;
    /** HTTPS URL of the update archive (zip or tar.gz) */
    public String url;
    /** Optional hex SHA-256 of the archive */
    public String sha256;
    /** Optional: expected size, bytes */
    public Long sizeBytes;
    /** Optional: explicit launch command to run after update */
    public List<String> launchCommand;

    public String archiveFileName() {
        if (url == null || url.isBlank()) return "update.bin";
        try { return Paths.get(new java.net.URI(url).getPath()).getFileName().toString(); }
        catch (Exception e) { return url.substring(url.lastIndexOf('/') + 1); }
    }
}
