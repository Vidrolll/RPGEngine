package com.rpg.main.graphics.opengl;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.rpg.main.Game;
import com.rpg.main.graphics.Shaders;
import com.rpg.main.graphics.particle.ParticleHandler;
import com.rpg.main.util.Time;
import org.lwjgl.openal.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.openal.AL10.AL_INVERSE_DISTANCE;
import static org.lwjgl.openal.AL10.alDistanceModel;
import static org.lwjgl.openal.ALC10.*;

public class EventListener implements GLEventListener {
    Game game;

    //Variables for the audio device
    private static long audioContext;
    private static long audioDevice;

    public EventListener(Game game) {
        this.game = game;
    }

    /**
     * Creates a new audio device.
     */
    public static void createAudioContext() {
        String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        assert defaultDeviceName != null : "No audio device found.";
        audioDevice = alcOpenDevice(defaultDeviceName);

        int[] attributes = {0};
        audioContext = alcCreateContext(audioDevice,attributes);
        alcMakeContextCurrent(audioContext);

        ALCCapabilities alcCapabilities = ALC.createCapabilities(audioDevice);
        ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);

        assert alCapabilities.OpenAL10 : "Audio library not supported.";
    }

    /**
     * Automatically ran when window is closed.
     * @param drawable (GLAutoDrawable) The drawable context of the window.
     */
    @Override
    public void dispose(GLAutoDrawable drawable) {
        cleanupAudioDevices();
        drawable.getAnimator().stop();
    }

    /**
     * Cleans up all the audio devices after the game has been run
     * to prevent initialization errors of the audio context on future runs.
     */
    public void cleanupAudioDevices() {
        alcDestroyContext(audioContext);
        alcCloseDevice(audioDevice);
    }

    /**
     * Automatically ran everytime the screen is rendered. Called every frame.
     * @param drawable (GLAutoDrawable) The drawable context of the window.
     */
    @Override
    public void display(GLAutoDrawable drawable) {
        deltaTime();
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        game.camera(gl);
        game.update();
        ParticleHandler.update();
        game.draw(gl);
        ParticleHandler.render(gl);
        gl.glLoadIdentity();
        game.drawGUI(gl);
    }

    /**
     * Determines how much game objects should be updated compared to the last run of the game loop.
     */
    long lastTime = System.nanoTime();
    public void deltaTime() {
        long now = System.nanoTime();
        Time.deltaTime = (now-lastTime)/(1000000000.0D/Time.TPS);
        lastTime = now;
    }

    /**
     * Automatically called everytime the window size is changed in any way.
     * @param drawable the triggering {@link GLAutoDrawable}
     * @param x viewport x-coord in pixel units
     * @param y viewport y-coord in pixel units
     * @param width viewport width in pixel units
     * @param height viewport height in pixel units
     */
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0, Renderer.screenWidth, Renderer.screenHeight, 0, -1, 1);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    /**
     * Automatically ran upon creation of a new window.
     *
     * @param drawable (GLAutoDrawable) The drawable context of the window.
     */
    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        glfwInit();
        Shaders.createShader(gl,"lighting");
        Shaders.createShader(gl,"wrap");
        alDistanceModel(AL_INVERSE_DISTANCE);
        AL10.alListener3f(AL10.AL_POSITION, 0, 0, 0);
        AL10.alDistanceModel(AL10.AL_INVERSE_DISTANCE);
        gl.setSwapInterval(1);
        gl.glClearColor(0, 0, 0, 1);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0, Renderer.screenWidth, Renderer.screenHeight, 0, -1, 1);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        for (int jid = GLFW_JOYSTICK_1; jid <= GLFW_JOYSTICK_LAST; jid++) {
            if (glfwJoystickPresent(jid)) {
                System.out.println("Joystick " + jid + " is present: " + glfwGetJoystickName(jid));
            }
        }
    }
}
