package com.bitwave.ve3;

import com.bitwave.ve3.platform.GameLoop;
import com.bitwave.ve3.platform.Window;
import com.bitwave.ve3.platform.WindowConfig;

/**
 * Main entrypoint/project root class.
 */
public class Main {
    public static void main(String[] args) {
        Window window = new Window();
        window.create(WindowConfig.defaults());

        GameLoop loop = new GameLoop(window, new TestApplication());
        loop.run();

        window.destroy();
    }
}
