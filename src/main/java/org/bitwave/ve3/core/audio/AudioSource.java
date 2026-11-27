package org.bitwave.ve3.core.audio;

import static org.lwjgl.openal.AL10.*;

public final class AudioSource implements AutoCloseable {
    private final int handle;
    private boolean closed;

    public AudioSource() {
        int generatedHandle = alGenSources();
        if (generatedHandle == 0) {
            throw new IllegalStateException("OpenAL failed to create an audio source");
        }

        handle = generatedHandle;

        alSource3f(handle, AL_POSITION, 0f, 0f, 0f);
        alSource3f(handle, AL_VELOCITY, 0f, 0f, 0f);
        alSourcef(handle, AL_PITCH, 1f);
        alSourcef(handle, AL_GAIN, 1f);
    }

    public void setBuffer(AudioBuffer buffer) {
        ensureOpen();
        alSourceStop(handle);
        alSourcei(handle, AL_BUFFER, buffer.handle());
    }

    public void play() {
        ensureOpen();
        alSourcePlay(handle);
    }

    public void pause() {
        ensureOpen();
        alSourcePause(handle);
    }

    public void stop() {
        ensureOpen();
        alSourceStop(handle);
    }

    public boolean isPlaying() {
        ensureOpen();
        return alGetSourcei(handle, AL_SOURCE_STATE) == AL_PLAYING;
    }

    public void setLooping(boolean looping) {
        ensureOpen();
        alSourcei(handle, AL_LOOPING, looping ? AL_TRUE : AL_FALSE);
    }

    public void setGain(float gain) {
        ensureOpen();
        if (!Float.isFinite(gain) || gain < 0f) {
            throw new IllegalArgumentException("Gain must be finite and non-negative: " + gain);
        }
        alSourcef(handle, AL_GAIN, gain);
    }

    public void setPitch(float pitch) {
        ensureOpen();
        if (!Float.isFinite(pitch) || pitch <= 0f) {
            throw new IllegalArgumentException("Pitch must be finite and positive: " + pitch);
        }
        alSourcef(handle, AL_PITCH, pitch);
    }

    public void setPosition(float x, float y, float z) {
        ensureOpen();
        alSource3f(handle, AL_POSITION, x, y, z);
    }

    public void setVelocity(float x, float y, float z) {
        ensureOpen();
        alSource3f(handle, AL_VELOCITY, x, y, z);
    }

    private void ensureOpen() {
        if (closed) {
            throw new IllegalStateException("Audio Source is closed");
        }
    }

    @Override
    public void close() throws Exception {
        if (closed) return;

        alSourceStop(handle);
        alSourcei(handle, AL_BUFFER, 0);
        alDeleteSources(handle);
        closed = true;
    }
}
