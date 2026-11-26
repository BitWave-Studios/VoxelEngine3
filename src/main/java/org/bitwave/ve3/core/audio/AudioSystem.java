package org.bitwave.ve3.core.audio;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public final class AudioSystem implements AutoCloseable {
    private long device;
    private long context;
    private boolean closed;

    public AudioSystem() {
        device = alcOpenDevice((ByteBuffer) null);
        if (device == NULL) {
            throw new IllegalStateException("Failed to open audio device");
        }

        try {
            ALCCapabilities alcCapabilities = ALC.createCapabilities(device);

            context = alcCreateContext(device, (IntBuffer) null);
            if (context == NULL) {
                throw new IllegalStateException("Failed to create OpenAL context");
            }

            if (!alcMakeContextCurrent(context)) {
                throw new IllegalStateException("Failed to make OpenAL context current");
            }

            AL.createCapabilities(alcCapabilities);
        } catch (RuntimeException e) {
            close();
            throw e;
        }
    }

    @Override
    public void close() {
        if (closed) return;

        closed = true;

        alcMakeContextCurrent(NULL);
        AL.setCurrentProcess(null);

        if (context != NULL) {
            alcDestroyContext(context);
            context = NULL;
        }

        if (device != NULL) {
            alcCloseDevice(device);
            device = NULL;
        }
    }
}
