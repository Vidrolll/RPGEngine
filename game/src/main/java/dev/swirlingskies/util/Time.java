package dev.swirlingskies.util;

public final class Time {
    private static long lastFrameTime;
    private static double deltaTime;
    private static double elapsedTime;
    private static int frameCount;
    private static double fps;
    private static double fpsTimer;

    private static float timeScale = 1.0f;

    public static void init() {
        lastFrameTime = System.nanoTime();
        elapsedTime = 0.0;
        frameCount = 0;
        fps = 0.0;
        fpsTimer = 0.0;
    }

    public static void update() {
        long now = System.nanoTime();
        deltaTime = (now - lastFrameTime) / 1_000_000_000.0;
        lastFrameTime = now;

        elapsedTime += deltaTime;
        frameCount++;

        // FPS tracking (once per second)
        fpsTimer += deltaTime;
        if (fpsTimer >= 1.0) {
            fps = frameCount / elapsedTime; // or keep a rolling counter
            fpsTimer = 0.0;
            frameCount = 0;
        }
    }

    public static double getDeltaTime() { return deltaTime * timeScale; }
    public static double getUnscaledDeltaTime() { return deltaTime; }
    public static double getElapsedTime() { return elapsedTime; }
    public static double getFPS() { return fps; }
    public static void setTimeScale(float scale) { timeScale = scale; }
    public static float getTimeScale() { return timeScale; }
}
