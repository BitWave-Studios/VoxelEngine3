package org.bitwave.ve3.demo;

import org.bitwave.ve3.core.Engine;
import org.bitwave.ve3.core.IAppLogic;
import org.bitwave.ve3.core.graph.Mesh;
import org.bitwave.ve3.core.render.Render;
import org.bitwave.ve3.core.scene.Scene;
import org.bitwave.ve3.core.window.Window;

/**
 * Main entrypoint for the testbench demo game.
 * @apiNote This is a simple demo game that shows how to use the engine.
 * It is not a complete game, and is not intended to be used as a template for a game.
 */
public class DemoGame implements IAppLogic {
    public static void main(String[] args) {
        DemoGame game = new DemoGame();
        Engine gameEng = new Engine("VE3", new Window.WindowOptions(), game);
        gameEng.start();
    }

    @Override
    public void cleanup() {

    }

    @Override
    public void init(Window window, Scene scene, Render render) {
        float[] positions = new float[] {
                -0.5f, 0.5f,-1.0f,
                -0.5f,-0.5f,-1.0f,
                 0.5f,-0.5f,-1.0f,
                 0.5f, 0.5f,-1.0f,
        };
        float[] colors = new float[] {
                 0.5f, 0.0f, 0.0f,
                 0.0f, 0.5f, 0.0f,
                 0.0f, 0.0f, 0.5f,
                 0.0f, 0.5f, 0.5f,
        };
        int[] indices = new int[] {
                0, 1, 3, 3, 1, 2
        };

        Mesh mesh = new Mesh(positions, colors, indices);
        scene.addMesh("quad", mesh);
    }

    @Override
    public void input(Window window, Scene scene, long diffTimeMillis) {

    }

    @Override
    public void update(Window window, Scene scene, long diffTimeMillis) {

    }
}
