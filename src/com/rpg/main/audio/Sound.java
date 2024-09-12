package com.rpg.main.audio;

import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.libc.LibCStdlib;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class Sound {
    private int bufferId;
    private int sourceId;
    private String filepath;

    private boolean isPlaying = false;

    /**
     *Creates a new sound file to play.
     * @param filepath (String) The path of the sound to create.
     * @param loops (Boolean) Whether the sound should loop.
     */
    public Sound(String filepath, boolean loops) {
        this.filepath = filepath;
        MemoryStack.stackPush();
        IntBuffer channelsBuffer = MemoryStack.stackMallocInt(1);
        MemoryStack.stackPush();
        IntBuffer sampleRateBuffer = MemoryStack.stackMallocInt(1);
        ShortBuffer rawAudioBuffer = STBVorbis.stb_vorbis_decode_filename(filepath, channelsBuffer, sampleRateBuffer);
        if(rawAudioBuffer==null) {
            System.err.println("Could not load sound '"+filepath+"'");
            MemoryStack.stackPop();
            MemoryStack.stackPop();
            return;
        }
        int channels = channelsBuffer.get();
        int sampleRate = sampleRateBuffer.get();
        MemoryStack.stackPop();
        MemoryStack.stackPop();
        int format = switch(channels) {
            case 1: yield AL10.AL_FORMAT_MONO16;
            case 2: yield AL10.AL_FORMAT_STEREO16;
            default: yield -1;
        };
        bufferId = AL10.alGenBuffers();
        AL10.alBufferData(bufferId, format, rawAudioBuffer, sampleRate);
        sourceId = AL10.alGenSources();
        AL10.alSourcei(sourceId, AL10.AL_BUFFER, bufferId);
        AL10.alSourcei(sourceId, AL10.AL_LOOPING, loops ? 1 : 0);
        AL10.alSourcei(sourceId, AL10.AL_POSITION, 0);
        AL10.alSourcef(sourceId, AL10.AL_GAIN, 0.3f);
        LibCStdlib.free(rawAudioBuffer);
    }

    /**
     * Destroys the audio object.
     */
    public void delete() {
        AL10.alDeleteSources(sourceId);
        AL10.alDeleteBuffers(bufferId);
    }

    /**
     * Plays the audio file.
     */
    public void play() {
        int state = AL10.alGetSourcei(sourceId, AL10.AL_SOURCE_STATE);
        if(state == AL10.AL_STOPPED) {
            isPlaying = false;
            AL10.alSourcei(sourceId, AL10.AL_POSITION, 0);
        }
        if(!isPlaying) {
            AL10.alSourcePlay(sourceId);
            isPlaying = true;
        }
    }

    /**
     * Stops the audio file.
     */
    public void stop() {
        if(isPlaying) {
            AL10.alSourceStop(sourceId);
            isPlaying = false;
        }
    }

    /**
     * Returns the path of the loaded audio file.
     * @return (String) The audio file path.
     */
    public String getFilepath() {
        return filepath;
    }

    /**
     * Returns if the audio file is playing.
     * @return (Boolean) True if the audio file is playing.
     */
    public boolean isPlaying() {
        int state = AL10.alGetSourcei(sourceId, AL10.AL_SOURCE_STATE);
        if(state == AL10.AL_STOPPED) isPlaying = false;
        return isPlaying;
    }

    /**
     * Sets the gain of the audio playing.
     * @param gain (Float) The new gain of the audio.
     */
    public void setGain(float gain) {
        AL10.alSourcef(sourceId, AL10.AL_GAIN, gain);
    }

    /**
     * Sets the pan of the audio playing.
     * @param pan (Float) The pan of the audio, clamped to [-1,1]
     */
    public void setPan(float pan) {
        pan = Math.max(-1,Math.min(pan,1));
        AL10.alSourcef(sourceId, AL10.AL_ROLLOFF_FACTOR, 0.0f);
        AL10.alSourcei(sourceId,AL10.AL_SOURCE_RELATIVE,AL10.AL_TRUE);
        AL10.alSourcefv(sourceId,AL10.AL_POSITION, new float[]{pan, 0, -(float)Math.sqrt(1 - pan * pan)});
    }
}
