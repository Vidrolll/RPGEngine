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
            System.err.print("Could not load sound '"+filepath+"'");
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
        AL10.alSourcef(sourceId, AL10.AL_GAIN, 0.3f);
        LibCStdlib.free(rawAudioBuffer);
    }

    public void delete() {
        AL10.alDeleteSources(sourceId);
        AL10.alDeleteBuffers(bufferId);
    }

    public void play() {
        int state = AL10.alGetSourcei(sourceId, AL10.AL_SOURCE_STATE);
        if(state == AL10.AL_STOPPED) {
            isPlaying = false;
        }
        if(!isPlaying) {
            AL10.alSourcePlay(sourceId);
            isPlaying = true;
        }
    }

    public void stop() {
        if(isPlaying) {
            AL10.alSourceStop(sourceId);
            isPlaying = false;
        }
    }

    public String getFilepath() {
        return filepath;
    }

    public boolean isPlaying() {
        int state = AL10.alGetSourcei(sourceId, AL10.AL_SOURCE_STATE);
        if(state == AL10.AL_STOPPED) isPlaying = false;
        return isPlaying;
    }

    public void setGain(float gain) {
        AL10.alSourcef(sourceId, AL10.AL_GAIN, gain);
    }

    public void setPan(float pan) {
        AL10.alSourcef(sourceId, AL10.AL_ROLLOFF_FACTOR, 0.0f);
        AL10.alSourcei(sourceId,AL10.AL_SOURCE_RELATIVE,AL10.AL_TRUE);
        AL10.alSourcefv(sourceId,AL10.AL_POSITION, new float[]{pan, 0, -(float)Math.sqrt(1 - pan * pan)});
    }
    public void setPan(float panX, float panY) {
        AL10.alSourcef(sourceId, AL10.AL_ROLLOFF_FACTOR, 0.0f);
        AL10.alSourcei(sourceId,AL10.AL_SOURCE_RELATIVE,AL10.AL_TRUE);
        AL10.alSourcefv(sourceId,AL10.AL_POSITION, new float[]{panX, panY, -(float)Math.sqrt(1 - panX * panX)});
    }
}