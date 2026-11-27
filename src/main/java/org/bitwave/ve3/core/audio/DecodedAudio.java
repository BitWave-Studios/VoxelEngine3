package org.bitwave.ve3.core.audio;

public record DecodedAudio(short[] samples, int channels, int sampleRate) {
    public DecodedAudio {
        if (samples == null || samples.length == 0) {
            throw new IllegalArgumentException("Audio samples must not be empty");
        }

        if (channels != 1 && channels != 2) {
            throw new IllegalArgumentException("Audio channels must be either 1 (mono) or 2 (stereo)");
        }

        if (sampleRate <= 0) {
            throw new IllegalArgumentException("Sample rate must be a positive integer");
        }

        if (samples.length % channels != 0) {
            throw new IllegalArgumentException("Sample count must be divisible by channel count");
        }
    }
}
