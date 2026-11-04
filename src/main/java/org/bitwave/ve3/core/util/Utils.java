package org.bitwave.ve3.core.util;

import java.io.IOException;
import java.io.InputStream;

/**
 * Utility class for reading files.
 * @author k4i
 */
public class Utils {
    private Utils() {}

    public static String readFile(String filePath) {
        try (InputStream in = Utils.class.getClassLoader().getResourceAsStream(filePath)) {
            if (in == null) {
                throw new IllegalArgumentException("File not found: " + filePath);
            }
            return new String(in.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + filePath, e);
        }
    }

    public static byte[] readBinaryFiles(String filePath) {
        try (InputStream in = Utils.class.getClassLoader().getResourceAsStream(filePath)) {
            if (in == null) {
                throw new IllegalArgumentException("File not found: " + filePath);
            }
            return in.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + filePath, e);
        }
    }
}
