package org.bitwave.ve3.demo;

import org.bitwave.ve3.core.player.Player;
import org.bitwave.ve3.core.render.Renderer;
import org.bitwave.ve3.core.window.Window;
import org.bitwave.ve3.core.world.World;

public class DemoGame {
    public static void main(String[] args) {
        Window window = new Window(1280, 720, "VE3 Demo");
        window.init();

        Renderer renderer = new Renderer();
        renderer.init();

        World world = new World(32, 8, 32);
        world.generateFlatWorld();

        Player player = new Player(16f, 4.2f, 16f, window);
        player.getCamera().setYaw(0f);
        player.getCamera().setPitch(-10f);

        while (!window.shouldClose()) {
            window.pollEvents();
            player.update((float) window.getDeltaTime(), world);
            renderer.render(world, player.getCamera());
            window.swapBuffers();
        }

        renderer.cleanup();
        window.cleanup();
    }
}
