package org.bitwave.ve3.core;

import org.bitwave.ve3.core.render.Render;
import org.bitwave.ve3.core.scene.Scene;
import org.bitwave.ve3.core.window.Window;

public class Engine {
    public static final int TARGET_UPS = 30;
    private final IAppLogic appLogic;
    private final Window window;
    private Render render;
    private boolean running;
    private Scene scene;
    private int targetFps;
    private int targetUps;

    public Engine(String title, Window.WindowOptions options, IAppLogic appLogic) {
        window = new Window(title, options, () -> {
            resize();
            return null;
        });

        targetFps = options.fps;
        targetUps = options.ups;

        this.appLogic = appLogic;
        render = new Render();
        scene = new Scene(window.getWidth(), window.getHeight());

        appLogic.init(window, scene, render);
        running = true;

    }

    private void cleanup() {
        appLogic.cleanup();
        render.cleanup();
        scene.cleanup();
        window.cleanup();
    }

    private void resize() {
        scene.resize(window.getWidth(), window.getHeight());
    }

    private void run() {
        long initTime = System.currentTimeMillis();
        float timeU = 1000.0f / targetUps;
        float timeR = targetFps > 0 ? 1000.0f / targetFps : 0;
        float deltaUpdate = 0;
        float deltaFps = 0;

        long updateTime = initTime;
        while (running && !window.windowShouldClose()) {
            window.pollEvents();

            long now = System.currentTimeMillis();
            deltaUpdate += (now - initTime) / timeU;
            deltaFps += (now - initTime) / timeR;

            if (targetFps <= 0 || deltaFps >= 1) {
                window.getMouseInput().input();
                appLogic.input(window, scene, now - initTime);
            }

            if (deltaUpdate >= 1) {
                long diffTimeMillis = now - updateTime;
                appLogic.update(window, scene, diffTimeMillis);
                updateTime = now;
                deltaUpdate--;
            }

            if (targetFps <= 0 || deltaFps >= 1) {
                render.render(window, scene);
                deltaFps--;
                window.update();
            }
            initTime = now;
        }

        cleanup();
    }

    public void start() {
        running = true;
        run();
    }

    public void stop() {
        running = false;
    }
}
