package com.rpg.main.opengl;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.rpg.main.Game;
import org.lwjgl.openal.*;

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
        createAudioContext();
        gl.glClearColor(0,0,0,1);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        ALC10.alcDestroyContext(audioContext);
        ALC10.alcCloseDevice(audioDevice);
        drawable.getAnimator().stop();
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        game.update();
        game.draw(gl);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        gl.glOrtho(0,1920,1080,0,-1,1);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    /**
     * Creates a new audio device.
     */
    public void createAudioContext() {
        String defaultDeviceName = ALC10.alcGetString(0, ALC10.ALC_DEFAULT_DEVICE_SPECIFIER);
        assert defaultDeviceName != null : "No audio device found.";
        audioDevice = ALC10.alcOpenDevice(defaultDeviceName);

        int[] attributes = {0};
        audioContext = ALC10.alcCreateContext(audioDevice,attributes);
        ALC10.alcMakeContextCurrent(audioContext);

        ALCCapabilities alcCapabilities = ALC.createCapabilities(audioDevice);
        ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);

        assert alCapabilities.OpenAL10 : "Audio library not supported.";
    }
}
