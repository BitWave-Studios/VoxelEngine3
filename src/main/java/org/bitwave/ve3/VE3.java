package org.bitwave.ve3;

import org.lwjgl.sdl.SDLError;
import org.lwjgl.sdl.SDLTimer;
import org.lwjgl.sdl.SDLMisc;

import java.net.URI;
import java.nio.file.Path;

public class VE3 {

    public static double getTime() {
        return (double) SDLTimer.SDL_GetTicksNS();
    }

    private VE3() {}

    public static void openUri(URI uri) {
        if (!SDLMisc.SDL_OpenURL(uri.toString())) {
            System.err.println("Failed to open URI: " + uri + " + " + SDLError.SDL_GetError());
        }
    }

    public static void openPath(Path path) {
        openUri(path.normalize().toUri());
    }
}
