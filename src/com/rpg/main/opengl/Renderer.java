package com.rpg.main.opengl;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;
import com.rpg.main.Game;
import com.rpg.main.util.InputHandler;
import org.lwjgl.openal.*;

import java.net.URL;

public class Renderer {
    //Variables for the window data
    private static GLWindow window;
    private static GLProfile profile;

    //Variables for the resolution data
    public static int screenWidth = 640;
    public static int screenHeight = 360;

    /**
     * Initialize a window using OpenGL
     */
    public static void init(Game game) {
        GLProfile.initSingleton();
        profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities caps = new GLCapabilities(profile);

        window = GLWindow.create(caps);
        window.setSize(1920,1080);
        window.setResizable(true);
        window.addGLEventListener(new EventListener(game));
        window.addMouseListener(new InputHandler(game));
        window.addKeyListener(new InputHandler(game));

        FPSAnimator animator = new FPSAnimator(window,60);
        animator.start();

        window.setFullscreen(true);
        window.setVisible(true);
        window.requestFocus();
    }



    /**
     * Returns the GLProfile.
     * @return (GLProfile) The current profile.
     */
    public static GLProfile getProfile() {
        return profile;
    }

    /**
     * Returns the current GLWindow.
     * @return (GLWindow) The current window.
     */
    public static GLWindow getWindow() {
        return window;
    }
}
