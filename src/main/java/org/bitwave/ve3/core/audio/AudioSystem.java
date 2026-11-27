package org.bitwave.ve3.core.audio;

import org.joml.Vector3f;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.openal.AL10.*;
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

    public void setListener(Vector3f position, Vector3f forward, Vector3f up) {
        if (closed) throw new IllegalStateException("Audio system is closed");

        alListener3f(AL_POSITION, position.x, position.y, position.z);
        alListener3f(AL_VELOCITY, 0f, 0f, 0f);

        try (MemoryStack stack = MemoryStack.stackPush()) {
            var orientation = stack.mallocFloat(6);
            orientation.put(forward.x).put(forward.y).put(forward.z);
            orientation.put(up.x).put(up.y).put(up.z);
            orientation.flip();

            alListenerfv(AL_ORIENTATION, orientation);
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
