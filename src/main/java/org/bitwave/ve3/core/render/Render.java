package org.bitwave.ve3.core.render;

import org.lwjgl.opengl.GL;
import org.bitwave.ve3.core.scene.Scene;
import org.bitwave.ve3.core.window.Window;

import static org.lwjgl.opengl.GL11.*;

public class Render {
    private SceneRender sceneRender;
    private SkyBoxRender skyBoxRender;

    public Render() {
        GL.createCapabilities();

        glDisable(GL_CULL_FACE);
        glDepthMask(false);
        glDepthFunc(GL_LEQUAL);

        skyBoxRender = new SkyBoxRender();

        glDepthFunc(GL_LESS);
        glDepthMask(true);

//        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        sceneRender = new SceneRender();
    }

    public SceneRender getSceneRender() {
        return sceneRender;
    }

    public void cleanup() {
        sceneRender.cleanup();
    }

    public void render(Window window, Scene scene) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glViewport(0, 0, window.getWidth(), window.getHeight());

        skyBoxRender.render(scene);
        sceneRender.render(scene);
    }
}
