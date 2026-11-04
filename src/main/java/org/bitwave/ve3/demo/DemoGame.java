package org.bitwave.ve3.demo;

import org.bitwave.ve3.core.Engine;
import org.bitwave.ve3.core.IAppLogic;
import org.bitwave.ve3.core.graph.Material;
import org.bitwave.ve3.core.graph.Mesh;
import org.bitwave.ve3.core.graph.Model;
import org.bitwave.ve3.core.graph.Texture;
import org.bitwave.ve3.core.input.MouseInput;
import org.bitwave.ve3.core.player.Camera;
import org.bitwave.ve3.core.render.Render;
import org.bitwave.ve3.core.scene.Entity;
import org.bitwave.ve3.core.scene.ModelLoader;
import org.bitwave.ve3.core.scene.Scene;
import org.bitwave.ve3.core.util.ResourcePaths;
import org.bitwave.ve3.core.util.Utils;
import org.bitwave.ve3.core.util.Version;
import org.bitwave.ve3.core.window.Window;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Main entrypoint for the testbench demo game.
 * @apiNote This is a simple demo game that shows how to use the engine.
 * It is not a complete game, and is not intended to be used as a template for a game.
 */
public class DemoGame implements IAppLogic {
    private static final float MOUSE_SENSITIVITY = 0.1f;
    private static final float MOVEMENT_SPEED = 0.005f;
    private Entity cube;
    private float rotation;

    public static void main(String[] args) {
        DemoGame game = new DemoGame();
        Engine gameEng = new Engine("VE3", new Window.WindowOptions(), game);
        gameEng.start();
    }

    @Override
    public void cleanup() {
        // Nothing to clean up yet
    }

    @Override
    public void init(Window window, Scene scene, Render render) {
        Model cubeModel = ModelLoader.loadModel(
                "cubeModel",
                ResourcePaths.toFilePath("models/cube/cube.obj"),
                scene.getTextureCache()
        );

        scene.addModel(cubeModel);

        cube = new Entity("cubeEntity", cubeModel.getId());
        cube.setPosition(0, 0, -2);
        scene.addEntity(cube);
    }

    @Override
    public void input(Window window, Scene scene, long diffTimeMillis) {
        float move = diffTimeMillis * MOVEMENT_SPEED;
        Camera camera = scene.getCamera();
        if (window.isKeyPressed(GLFW_KEY_W)) {
            camera.moveForward(move);
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            camera.moveBackwards(move);
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            camera.moveLeft(move);
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            camera.moveRight(move);
        }
        if (window.isKeyPressed(GLFW_KEY_UP)) {
            camera.moveUp(move);
        } else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            camera.moveDown(move);
        }

        MouseInput mouseInput = window.getMouseInput();
        if (mouseInput.isRightButtonPressed()) {
            Vector2f displVec = mouseInput.getDisplVec();
            camera.addRotation((float) Math.toRadians(-displVec.x * MOUSE_SENSITIVITY),
                    (float) Math.toRadians(-displVec.y) * MOUSE_SENSITIVITY);
        }
    }

    @Override
    public void update(Window window, Scene scene, long diffTimeMillis) {
        rotation += 1.5;
        if (rotation > 360) {
            rotation = 0;
        }
        cube.setRotation(1, 1, 1, (float) Math.toRadians(rotation));
        cube.updateModelMatrix();
    }
}
