package com.rpg.main.math;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

public class Calculus {
    static {
        InputStream libPath = Calculus.class.getResourceAsStream("/Calculus.dll");
        if (libPath != null) {
            try {
                Path tempFile = Files.createTempFile("nativeLibrary", ".dll");

                // Write the InputStream to the temporary file
                try (OutputStream outputStream = Files.newOutputStream(tempFile)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = libPath.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }

                System.load(tempFile.toAbsolutePath().toString());
            } catch (IOException e) {
                System.err.print("DLL not found in JAR.");
            }
        }
    }

    public static double derivative(Function<Double, Double> f, double t, double h) {
        return (f.apply(t + h) - f.apply(t - h)) / (2 * h);
    }

    public static double integrateTrapezoidal2(Function<Double, Double> f, double a, double b, int n) {
        double h = (b - a) / n;
        double sum = 0.5 * (f.apply(a) + f.apply(b));
        for (int i = 1; i < n; i++) {
            sum += f.apply(a + i * h);
        }
        return sum * h;
    }

    public native static double integrateTrapezoidal(Function<Double, Double> f, double a, double b, int n);

    public native static double[] rungeKutta4(double y0, double v0, double t, double dt, Function<Double, Double> f_v, Function<Double, Double> f_a);
}
