package org.bitwave.ve3.core.audio;

import org.lwjgl.system.MemoryUtil;

import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;

public final class AudioBuffer implements AutoCloseable {
    private final int handle;
    private boolean closed;

    public AudioBuffer(DecodedAudio audio) {
        int format = switch (audio.channels()) {
            case 1 -> AL_FORMAT_MONO16;
            case 2 -> AL_FORMAT_STEREO16;
            default -> throw new IllegalArgumentException("Unsupported channel count: " + audio.channels());
        };

        int generatedHandle = alGenBuffers();
        if (generatedHandle == 0) {
            throw new IllegalStateException("Failed to generate OpenAL buffer");
        }

        ShortBuffer pcm = MemoryUtil.memAllocShort(audio.samples().length);
        try {
            pcm.put(audio.samples()).flip();
            alBufferData(generatedHandle, format, pcm, audio.sampleRate());

            int error = alGetError();
            if (error != AL_NO_ERROR) {
                throw new IllegalStateException("OpenAL failed to upload audio data (error 0x" + Integer.toHexString(error) + ")");
            }

            handle = generatedHandle;
        } catch (RuntimeException e) {
            alDeleteBuffers(generatedHandle);
            throw e;
        } finally {
            MemoryUtil.memFree(pcm);
        }
    }

    public int handle() {
        if (closed) {
            throw new IllegalStateException("AudioBuffer has been closed");
        }

        return handle;
    }

    @Override
    public void close() throws Exception {
        if (closed) return;

        alDeleteBuffers(handle);
        closed = true;
    }
}
