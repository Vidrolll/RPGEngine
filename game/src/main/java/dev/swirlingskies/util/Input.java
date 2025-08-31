package dev.swirlingskies.util;

import org.lwjgl.glfw.GLFW;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public final class Input {
    // Live, main-thread-only state (mutated by GLFW callbacks)
    private static final Set<Integer> keysDown     = new HashSet<>();
    private static final Set<Integer> keysPressed  = new HashSet<>();
    private static final Set<Integer> keysReleased = new HashSet<>();
    private static final Set<Integer> mouseDown     = new HashSet<>();
    private static final Set<Integer> mousePressed  = new HashSet<>();
    private static final Set<Integer> mouseReleased = new HashSet<>();

    private static double mouseX, mouseY;
    private static double scrollX, scrollY;

    // Published snapshot (immutable view for any thread)
    private static final AtomicReference<FrameInput> snapshotRef = new AtomicReference<>();

    private Input() {}

    public static void init(long window) {
        GLFW.glfwSetKeyCallback(window, (win, key, sc, action, mods) -> {
            if (key == GLFW.GLFW_KEY_UNKNOWN) return;
            if (action == GLFW.GLFW_PRESS)  { keysDown.add(key);    keysPressed.add(key); }
            if (action == GLFW.GLFW_RELEASE){ keysDown.remove(key); keysReleased.add(key); }
        });

        GLFW.glfwSetMouseButtonCallback(window, (win, btn, action, mods) -> {
            if (action == GLFW.GLFW_PRESS)  { mouseDown.add(btn);    mousePressed.add(btn); }
            if (action == GLFW.GLFW_RELEASE){ mouseDown.remove(btn); mouseReleased.add(btn); }
        });

        GLFW.glfwSetCursorPosCallback(window, (win, x, y) -> { mouseX = x; mouseY = y; });

        GLFW.glfwSetScrollCallback(window, (win, sx, sy) -> { scrollX += sx; scrollY += sy; });
    }

    /** Call once per frame on the main thread, AFTER glfwPollEvents() and AFTER your game logic has read live input. */
    public static void publishSnapshot() {
        // Copy into new sets so workers see a stable frame view.
        var snap = new FrameInput(
                unmodifiableCopy(keysDown),
                unmodifiableCopy(keysPressed),
                unmodifiableCopy(keysReleased),
                unmodifiableCopy(mouseDown),
                unmodifiableCopy(mousePressed),
                unmodifiableCopy(mouseReleased),
                mouseX, mouseY, scrollX, scrollY
        );
        snapshotRef.set(snap); // atomic set = safe publication (happens-before)
    }

    /** Clear edge events; call right after publishSnapshot() at end of frame. */
    public static void endFrame() {
        keysPressed.clear();
        keysReleased.clear();
        mousePressed.clear();
        mouseReleased.clear();
        scrollX = scrollY = 0.0;
    }

    // -------- live queries (main thread) ----------
    public static boolean isKeyDown(int key)     { return keysDown.contains(key); }
    public static boolean isKeyPressed(int key)  { return keysPressed.contains(key); }
    public static boolean isKeyReleased(int key) { return keysReleased.contains(key); }

    public static boolean isMouseDown(int b)     { return mouseDown.contains(b); }
    public static boolean isMousePressed(int b)  { return mousePressed.contains(b); }
    public static boolean isMouseReleased(int b) { return mouseReleased.contains(b); }

    public static double mouseX() { return mouseX; }
    public static double mouseY() { return mouseY; }
    public static double scrollX() { return scrollX; }
    public static double scrollY() { return scrollY; }

    // -------- snapshot for workers ----------
    public static FrameInput snapshot() { return snapshotRef.get(); }

    private static <T> Set<T> unmodifiableCopy(Set<T> src) {
        // IMPORTANT: new HashSet<>(src) creates a distinct set for the snapshot.
        return Collections.unmodifiableSet(new HashSet<>(src));
    }

    // Immutable frame view
    public record FrameInput(
            Set<Integer> keysDown,
            Set<Integer> keysPressed,
            Set<Integer> keysReleased,
            Set<Integer> mouseDown,
            Set<Integer> mousePressed,
            Set<Integer> mouseReleased,
            double mouseX, double mouseY,
            double scrollX, double scrollY
    ) {}
}
