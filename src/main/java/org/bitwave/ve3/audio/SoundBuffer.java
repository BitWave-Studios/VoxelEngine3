package org.bitwave.ve3.audio;


import java.nio.ByteBuffer;
import java.util.OptionalInt;
import javax.annotation.Nullable;
import javax.sound.sampled.AudioFormat;
import org.lwjgl.openal.AL10;

public class SoundBuffer {
    private @Nullable ByteBuffer data;
    private final AudioFormat format;
    private boolean hasAlBuffer;
    private int alBuffer;
    private final int size;

    public SoundBuffer(final ByteBuffer data, final AudioFormat format) {
        this.data = data;
        this.format = format;
        this.size = data.limit();
    }

    OptionalInt getAlBuffer() {
        if (!this.hasAlBuffer) {
            if (this.data == null) {
                return OptionalInt.empty();
            }

            int audioFormat = OpenALUtil.audioFormatToOpenAl(this.format);
            int[] intBuffer = new int[1];
            AL10.alGenBuffers(intBuffer);
            if (OpenALUtil.checkALError("Creating buffer")) {
                return OptionalInt.empty();
            }

            AL10.alBufferData(intBuffer[0], audioFormat, this.data, (int)this.format.getSampleRate());
            if (OpenALUtil.checkALError("Assigning buffer data")) {
                return OptionalInt.empty();
            }

            this.alBuffer = intBuffer[0];
            this.hasAlBuffer = true;
            this.data = null;
        }

        return OptionalInt.of(this.alBuffer);
    }

    public void discardAlBuffer() {
        if (this.hasAlBuffer) {
            AL10.alDeleteBuffers(new int[]{this.alBuffer});
            if (OpenALUtil.checkALError("Deleting stream buffers")) {
                return;
            }
        }

        this.hasAlBuffer = false;
    }

    public OptionalInt releaseAlBuffer() {
        OptionalInt result = this.getAlBuffer();
        this.hasAlBuffer = false;
        return result;
    }

    public AudioFormat format() {
        return this.format;
    }

    public int size() {
        return this.size;
    }

    public boolean isValid() {
        return this.data != null || this.hasAlBuffer;
    }
}
