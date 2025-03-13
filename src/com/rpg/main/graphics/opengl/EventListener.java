package com.rpg.main.graphics.opengl;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.rpg.main.Game;
import com.rpg.main.graphics.Shaders;
import com.rpg.main.util.Time;
import org.lwjgl.openal.*;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;

public class EventListener implements GLEventListener {
    Game game;

    //Variables for the audio device
    private long audioContext;
    private long audioDevice;

    public EventListener(Game game) {
        this.game = game;
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        Shaders.createShader(gl,"lighting");
        Shaders.createShader(gl,"wrap");

        gl.setSwapInterval(1);
        createAudioContext();
        gl.glClearColor(0,0,0,1);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        cleanupAudioDevices();
        drawable.getAnimator().stop();
    }

    public void cleanupAudioDevices() {
        alcDestroyContext(audioContext);
        alcCloseDevice(audioDevice);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        deltaTime();
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        game.camera(gl);
        game.update();
        game.draw(gl);
        gl.glPopMatrix();
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

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        gl.glOrtho(-10,10,10,-10,-1,1);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    /**
     * Creates a new audio device.
     */
    public void createAudioContext() {
        String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        assert defaultDeviceName != null : "No audio device found.";
        audioDevice = alcOpenDevice(defaultDeviceName);

        int[] attributes = {0};
        audioContext = alcCreateContext(audioDevice,attributes);
        alcMakeContextCurrent(audioContext);

        ALCCapabilities alcCapabilities = ALC.createCapabilities(audioDevice);
        ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);

        alDistanceModel(AL_INVERSE_DISTANCE);

        AL10.alListener3f(AL10.AL_POSITION,0,0,0);
        AL10.alDistanceModel(AL10.AL_INVERSE_DISTANCE);

        assert alCapabilities.OpenAL10 : "Audio library not supported.";
    }
}
