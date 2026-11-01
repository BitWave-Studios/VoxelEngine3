package org.bitwave.ve3.demo;

import org.bitwave.ve3.core.player.Player;
import org.bitwave.ve3.core.render.Renderer;
import org.bitwave.ve3.core.window.Window;
import org.bitwave.ve3.core.world.World;

/**
 * Main entrypoint for the testbench demo game.
 * @apiNote This is a simple demo game that shows how to use the engine.
 * It is not a complete game, and is not intended to be used as a template for a game.
 */
public class DemoGame {
    public static void main(String[] args) {
        Window window = new Window(1280, 720, "VE3 Demo");
        window.init();

        Renderer renderer = new Renderer();
        renderer.init();

        World world = new World();
        world.generateFlatWorld(32, 32);

        Player player = new Player(16f, 8f, 16f, window);
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
