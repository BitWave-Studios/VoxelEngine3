package org.bitwave.ve3.demo;

import org.bitwave.ve3.core.Engine;
import org.bitwave.ve3.core.IAppLogic;
import org.bitwave.ve3.core.graph.Model;
import org.bitwave.ve3.core.input.MouseInput;
import org.bitwave.ve3.core.player.Camera;
import org.bitwave.ve3.core.render.Render;
import org.bitwave.ve3.core.render.SceneRender;
import org.bitwave.ve3.core.scene.Entity;
import org.bitwave.ve3.core.scene.ModelLoader;
import org.bitwave.ve3.core.scene.Scene;
import org.bitwave.ve3.core.scene.Skybox;
import org.bitwave.ve3.core.scene.lights.SceneLights;
import org.bitwave.ve3.core.util.ResourcePaths;
import org.bitwave.ve3.core.util.Version;
import org.bitwave.ve3.core.window.Window;
import org.bitwave.ve3.gui.*;
import org.bitwave.ve3.gui.components.Button;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Main entrypoint for the testbench demo game.
 * @apiNote This is a simple demo game that shows how to use the engine.
 * It is not a complete game, and is not intended to be used as a template for a game.
 */
public class DemoGame implements IAppLogic {
    private static final String engineVersion;

    static {
        try {
            engineVersion = new Version().getVersion();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static final float MOUSE_SENSITIVITY = 0.1f;
    private static final float MOVEMENT_SPEED = 0.005f;

    private static final String UI_VERTEX_SHADER = ResourcePaths.toResourcePath("/shaders/gui.vert");
    private static final String UI_FRAGMENT_SHADER = ResourcePaths.toResourcePath("/shaders/gui.frag");

    private final UiContext uiContext = new UiContext();
    private final Button toggleWireframeBtn = new Button(uiContext);

    private UiShader uiShader;
    private UiOpenGLRenderer uiRenderer;

    private boolean prevLeftMouseDown;
    private boolean prevRightMouseDown;

    private Entity cube;
    private float rotation;

    private SceneRender sceneRender;

    private boolean wireframeEnabled;

    public static void main(String[] args) {
        DemoGame game = new DemoGame();
        Engine gameEng = new Engine("VE3 - " + engineVersion, new Window.WindowOptions(), game);
        gameEng.start();
    }

    @Override
    public void cleanup() {
        if (uiRenderer != null) uiRenderer.cleanup();
        if (uiShader != null) uiShader.cleanup();
    }

    @Override
    public void init(Window window, Scene scene, Render render) {
        this.sceneRender = render.getSceneRender();

        SceneLights sceneLights = new SceneLights();
        sceneLights.getAmbientLight().setIntensity(0.8f);
        scene.setSceneLights(sceneLights);

        Skybox skybox = new Skybox(ResourcePaths.toResourcePath("/models/skybox/skybox.obj"), scene.getTextureCache());
        skybox.getSkyboxEntity().setScale(50);
        skybox.getSkyboxEntity().updateModelMatrix();
        scene.setSkybox(skybox);
        scene.getCamera().moveUp(0.1f);

        uiShader = new UiShader(UI_VERTEX_SHADER, UI_FRAGMENT_SHADER);
        uiRenderer = new UiOpenGLRenderer(uiShader);

        window.getMouseInput().setUiInput(uiContext.input);

        Model cubeModel = ModelLoader.loadModel(
                "cubeModel",
                ResourcePaths.toResourcePath("/models/cube/cube.obj"),
                scene.getTextureCache()
        );

        scene.addModel(cubeModel);

        cube = new Entity("cubeEntity", cubeModel.getId());
        cube.setPosition(0, 0, -2);
        scene.addEntity(cube);
    }

    @Override
    public void renderGui(Window window, Scene scene) {
        uiRenderer.render(
                uiContext.drawList,
                window.getWidth(),
                window.getHeight()
        );
    }

    @Override
    public void input(Window window, Scene scene, long diffTimeMillis) {
        uiContext.beginFrame(window.getWidth(), window.getHeight());

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

        if (window.isKeyPressed(GLFW_KEY_E)) {
            camera.moveUp(move);
        } else if (window.isKeyPressed(GLFW_KEY_Q)) {
            camera.moveDown(move);
        }

        MouseInput mouseInput = window.getMouseInput();

        if (mouseInput.isRightButtonPressed()) {
            Vector2f displVec = mouseInput.getDisplVec();
            camera.addRotation(
                    (float) Math.toRadians(-displVec.x * MOUSE_SENSITIVITY),
                    (float) Math.toRadians(-displVec.y) * MOUSE_SENSITIVITY);
        }

        boolean leftMouseDown = uiContext.input.isMouseDown(0);
        boolean leftPressed = leftMouseDown && !prevLeftMouseDown;
        prevLeftMouseDown = leftMouseDown;

        if (toggleWireframeBtn.draw("Toggle Wireframe", 24f, 24f, 180f, 34f)) {
            wireframeEnabled = !wireframeEnabled;
            sceneRender.setWireframe(wireframeEnabled);
            System.out.println("Wireframe mode: " + (wireframeEnabled ? "enabled" : "disabled"));
        }

        UiColor indicatorColor = !wireframeEnabled ? UiColor.rgba(90, 200, 120, 1) : UiColor.rgba(100, 70, 70, 1);
        uiContext.drawList.addFilledRect(new UiRect(24f, 66f, 18f, 18f), indicatorColor);

        uiContext.endFrame();
    }

    @Override
    public void update(Window window, Scene scene, long diffTimeMillis) {
        rotation += 1.5f;

        if (rotation > 360) {
            rotation = 0;
        }

        cube.setRotation(1, 1, 1, (float) Math.toRadians(rotation));
        cube.updateModelMatrix();
    }
}
