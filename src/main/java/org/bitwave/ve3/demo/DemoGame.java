package org.bitwave.ve3.demo;

import org.bitwave.ve3.core.Engine;
import org.bitwave.ve3.core.IAppLogic;
import org.bitwave.ve3.core.audio.*;
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
import org.bitwave.ve3.core.util.AppLog;
import org.bitwave.ve3.core.util.ResourcePaths;
import org.bitwave.ve3.core.util.Version;
import org.bitwave.ve3.core.window.Window;
import org.bitwave.ve3.gui.*;
import org.bitwave.ve3.gui.components.Button;
import org.bitwave.ve3.gui.font.StbUiFont;
import org.joml.Vector2f;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Main entrypoint for the testbench demo game.
 * @apiNote This is a simple demo game that shows how to use the engine.
 * It is not a complete game, and is not intended to be used as a template for a game.
 */
public class DemoGame implements IAppLogic {
    private static final String engineVersion;
    private static final String releaseType;
    private static final String publisher;
    private UiFont font;

    private static final boolean WRITE_LOG_ON_EXIT = true;

    static {
        try {
            engineVersion = new Version().getVersion();
            releaseType = new Version().getReleaseType();
            publisher = new Version().getPublisher();
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
    private AudioSystem audioSystem;
    private AudioBuffer audioBuffer;
    private AudioSource audioSource;

    public static void main(String[] args) {
        DemoGame game = new DemoGame();
        Engine gameEng = new Engine("VE3 - " + engineVersion, new Window.WindowOptions(), game);

        AppLog.info("Application started");

        gameEng.start();

        System.out.println("VE3 by BitWave Studios");
        System.out.println("Current release: " + engineVersion + "-" + releaseType);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (!WRITE_LOG_ON_EXIT) return;
            try {
                AppLog.writeTo(Path.of("application.log"));
            } catch (IOException e) {
                System.err.println("Could not write application log: " + e);
            }
        }, "write-log-on-exit"));
    }

    @Override
    public void cleanup() {
        if (uiRenderer != null) {
            uiRenderer.cleanup();
            AppLog.info("UIRenderer cleaned");
        }
        if (uiShader != null) {
            uiShader.cleanup();
            AppLog.info("UIShader cleaned");
        }
        if (audioSource != null) {
            try {
                audioSource.close();
            } catch (Exception e) {
                System.err.println("Failed to close audio source: " + e);
            }
        }
        if (audioBuffer != null) {
            try {
                audioBuffer.close();
            } catch (Exception e) {
                System.err.println("Failed to close audio buffer: " + e);
            }
        }
    }

    @Override
    public void init(Window window, Scene scene, Render render, AudioSystem audioSystem) {
        this.sceneRender = render.getSceneRender();
        this.audioSystem = audioSystem;

        String audioResource = ResourcePaths.toResourcePath("/audio/country-roads.wav");
        InputStream audioStream = DemoGame.class.getClassLoader().getResourceAsStream(audioResource);
        if (audioStream == null) {
            throw new RuntimeException("Audio resource not found: " + audioResource);
        }

        try {
            DecodedAudio decoded = AudioLoader.loadWav(audioStream);
            audioBuffer = new AudioBuffer(decoded);
            audioSource = new AudioSource();
            audioSource.setBuffer(audioBuffer);
        } catch (IOException | UnsupportedAudioFileException e) {
            throw new RuntimeException("Failed to load audio resource: " + audioResource, e);
        }

        SceneLights sceneLights = new SceneLights();
        sceneLights.getAmbientLight().setIntensity(0.8f);
        scene.setSceneLights(sceneLights);

        Skybox skybox = new Skybox(ResourcePaths.toResourcePath("/models/skybox/skybox.obj"), scene.getTextureCache());
        skybox.getSkyboxEntity().setScale(50);
        skybox.getSkyboxEntity().updateModelMatrix();
        scene.setSkybox(skybox);
        scene.getCamera().moveUp(0.1f);

        Camera camera = scene.getCamera();

        audioSystem.setListener(
                camera.getPosition(),
                camera.getDirection(),
                camera.getUp()
        );
        audioSource.setPosition(
                camera.getPosition().x,
                camera.getPosition().y,
                camera.getPosition().z
        );
        audioSource.play();

        uiShader = new UiShader(UI_VERTEX_SHADER, UI_FRAGMENT_SHADER);
        uiRenderer = new UiOpenGLRenderer(uiShader);
        font = new StbUiFont(ResourcePaths.toResourcePath("/fonts/Montserrat-Regular.ttf"));

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
            AppLog.info("Moving forward");
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            camera.moveBackwards(move);
            AppLog.info("Moving backwards");
        }

        if (window.isKeyPressed(GLFW_KEY_A)) {
            camera.moveLeft(move);
            AppLog.info("Moving left");
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            camera.moveRight(move);
            AppLog.info("Moving right");
        }

        if (window.isKeyPressed(GLFW_KEY_E)) {
            camera.moveUp(move);
            AppLog.info("Moving up");
        } else if (window.isKeyPressed(GLFW_KEY_Q)) {
            camera.moveDown(move);
            AppLog.info("Moving down");
        }

        MouseInput mouseInput = window.getMouseInput();

        if (mouseInput.isRightButtonPressed()) {
            Vector2f displVec = mouseInput.getDisplVec();
            camera.addRotation(
                    (float) Math.toRadians(-displVec.x * MOUSE_SENSITIVITY),
                    (float) Math.toRadians(-displVec.y) * MOUSE_SENSITIVITY);
        }

        audioSystem.setListener(camera.getPosition(), camera.getDirection(), camera.getUp());

        prevLeftMouseDown = uiContext.input.isMouseDown(0);

        if (toggleWireframeBtn.draw(UiId.named("toggle-wireframe"), font, "Toggle Wireframe", 24f, 24f, 180f, 34f, true).clicked()) {
            wireframeEnabled = !wireframeEnabled;
            sceneRender.setWireframe(wireframeEnabled);
            AppLog.info("Wireframe mode: " + (wireframeEnabled ? "enabled" : "disabled"));
        }

        UiColor indicatorColor = wireframeEnabled ? UiColor.rgba(90, 200, 120, 1) : UiColor.rgba(200, 70, 70, 1);
        uiContext.drawList.addFilledRect(new UiRect(24f, 66f, 18f, 18f), indicatorColor, 4f);

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
