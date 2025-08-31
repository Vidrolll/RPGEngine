package dev.swirlingskies.util;

import java.util.concurrent.*;

public final class TaskManager {
    private static ExecutorService pool;

    public static void init(int threads) {
        if (pool != null) return;
        // fixed pool is predictable; size ~ cores-1 is common
        pool = Executors.newFixedThreadPool(threads);
    }

    public static Future<?> runAsync(Runnable task) {
        return pool.submit(task);
    }

    public static <T> Future<T> callAsync(Callable<T> task) {
        return pool.submit(task);
    }

    public static void shutdown() {
        if (pool != null) pool.shutdownNow();
    }
}
