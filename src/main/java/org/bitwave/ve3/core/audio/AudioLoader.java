package org.bitwave.ve3.core.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public final class AudioLoader {
    private AudioLoader() {}

    public static DecodedAudio loadWav(Path path) throws IOException, UnsupportedAudioFileException {
        return loadWav(Files.newInputStream(path));
    }

    public static DecodedAudio loadWav(InputStream input) throws IOException, UnsupportedAudioFileException {
        try (AudioInputStream source = AudioSystem.getAudioInputStream(new BufferedInputStream(input))) {
            AudioFormat sourceFormat = source.getFormat();
            int channels = sourceFormat.getChannels();
            float sampleRate = sourceFormat.getSampleRate();

            if (channels != 1 && channels != 2) {
                throw new UnsupportedAudioFileException("Only mono and stereo WAV files are supported: " + channels + " channels");
            }

            if (!Float.isFinite(sampleRate) || sampleRate <= 0 || sampleRate > Integer.MAX_VALUE) {
                throw new UnsupportedAudioFileException("Invalid WAV sample rate: " + sampleRate);
            }

            AudioFormat pcmFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    sampleRate,
                    16,
                    channels,
                    channels * 2,
                    sampleRate,
                    false
            );

            try (AudioInputStream pcm = AudioSystem.getAudioInputStream(pcmFormat, source);
                 ByteArrayOutputStream output = new ByteArrayOutputStream()) {

                byte[] chunk = new byte[8192];
                int count;
                while ((count = pcm.read(chunk)) != -1) {
                    output.write(chunk, 0, count);
                }

                byte[] bytes = output.toByteArray();
                if (bytes.length == 0 || bytes.length % (channels * 2) != 0) {
                    throw new UnsupportedAudioFileException("WAV produced incomplete PCM frames");
                }

                short[] samples = new short[bytes.length / 2];
                for (int i = 0; i < samples.length; i++) {
                    int low = bytes[i * 2] & 0xFF;
                    int high = bytes[i * 2 + 1];
                    samples[i] = (short) ((high << 8) | low);
                }

                return new DecodedAudio(samples, channels, Math.round(sampleRate));
            }
        }
    }
}
