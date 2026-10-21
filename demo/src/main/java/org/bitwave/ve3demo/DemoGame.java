package org.bitwave.ve3demo;

import org.bitwave.ve3.Engine;
import org.bitwave.ve3desktop.Lwjgl3GameLoop;
import org.bitwave.ve3desktop.Window;

/**
 * Initial desktop test bench for VE3.
 */
public final class DemoGame {

    public static void main(String[] args) throws Exception {
        try (Window window = new Window(
                "VoxelEngine3 Demo",
                1280,
                720
        )) {
            Lwjgl3GameLoop loop = new Lwjgl3GameLoop(window);
            Engine engine = new Engine(loop);

            engine.start();
        }
    }
}
