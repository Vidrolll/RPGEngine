package dev.swirlingskies.graphics;

import dev.swirlingskies.Game;
import org.joml.Vector2f;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public final class Window {
    private long window;
    private int logicalW, logicalH;

    public void init() {
        if (!glfwInit()) throw new IllegalStateException("GLFW init failed");
        glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_COMPAT_PROFILE);
        glfwWindowHint(GLFW_SCALE_TO_MONITOR, GLFW_TRUE);
        logicalW = 1280;
        logicalH = 720;
        long monitor = glfwGetPrimaryMonitor();
        GLFWVidMode vid = glfwGetVideoMode(monitor);
        window = glfwCreateWindow(vid.width(), vid.height(), "RPGEngine", NULL, NULL);
        if (window == NULL) throw new RuntimeException("Window creation failed");
        try (MemoryStack stack = MemoryStack.stackPush()) {
            var pW = stack.mallocInt(1);
            var pH = stack.mallocInt(1);
            glfwGetWindowSize(window, pW, pH);
            var vm = glfwGetVideoMode(glfwGetPrimaryMonitor());
            if (vm != null) {
                glfwSetWindowPos(window, (vm.width() - pW.get(0)) / 2, (vm.height() - pH.get(0)) / 2);
            }
        }
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
        GL.createCapabilities();
        try (MemoryStack stack = MemoryStack.stackPush()) {
            var fbw = stack.mallocInt(1);
            var fbh = stack.mallocInt(1);
            glfwGetFramebufferSize(window, fbw, fbh);
            glViewport(0, 0, Math.max(1, fbw.get(0)), Math.max(1, fbh.get(0)));
        }
        glfwSetFramebufferSizeCallback(window, (win, w, h) -> glViewport(0, 0, Math.max(1, w), Math.max(1, h)));
    }

    public long getWindow() {
        return this.window;
    }

    public Vector2f getWindowDimensions() {
        return new Vector2f(logicalW,logicalH);
    }
}
