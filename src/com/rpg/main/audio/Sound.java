package com.rpg.main.audio;

import org.lwjgl.openal.AL10;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.MemoryStack;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;

public class Sound {
    private int bufferId;
    private int sourceId;
    private String filepath;

    private boolean isPlaying = false;

    /**
     * Creates a new sound file to play.
     * @param filepath (String) The path of the sound to create.
     * @param loops (Boolean) Whether the sound should loop.
     */
    public Sound(String filepath, boolean loops) {
        this.filepath = filepath;

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filepath)) {

            if (inputStream == null) {
                throw new IOException("Resource not found: " + filepath);
            }

            // Read the InputStream into a byte array
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            byte[] audioBytes = byteArrayOutputStream.toByteArray();

            // Decode the audio from the byte array
            try (MemoryStack stack = MemoryStack.stackPush()) {
                IntBuffer channelsBuffer = stack.mallocInt(1);
                IntBuffer sampleRateBuffer = stack.mallocInt(1);

                // Allocate a ByteBuffer and copy audioBytes into it
                ByteBuffer audioBuffer = ByteBuffer.allocateDirect(audioBytes.length);
                audioBuffer.put(audioBytes).flip();

                ShortBuffer rawAudioBuffer = STBVorbis.stb_vorbis_decode_memory(audioBuffer, channelsBuffer, sampleRateBuffer);

                if (rawAudioBuffer == null) {
                    throw new IOException("Could not decode audio data");
                }

                int channels = channelsBuffer.get();
                int sampleRate = sampleRateBuffer.get();
                int format = switch (channels) {
                    case 1 -> AL_FORMAT_MONO16;
                    case 2 -> AL_FORMAT_STEREO16;
                    default -> throw new IOException("Unsupported number of channels: " + channels);
                };

                // Generate and fill OpenAL buffer
                bufferId = alGenBuffers();
                alBufferData(bufferId, format, rawAudioBuffer, sampleRate);

                // Generate OpenAL source and configure it
                sourceId = alGenSources();
                alSourcei(sourceId, AL_BUFFER, bufferId);
                alSourcei(sourceId, AL_LOOPING, loops ? 1 : 0);
                alSourcei(sourceId, AL_POSITION, 0);
                alSourcef(sourceId, AL_GAIN, 0.3f);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    /**
     * Destroys the current audio source.
     */
    public void destroy() {
        AL10.alDeleteSources(sourceId);
    }
}
