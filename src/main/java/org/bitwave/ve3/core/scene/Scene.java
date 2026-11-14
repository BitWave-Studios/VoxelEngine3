package org.bitwave.ve3.core.scene;

import org.bitwave.ve3.core.graph.Model;
import org.bitwave.ve3.core.graph.TextureCache;
import org.bitwave.ve3.core.player.Camera;
import org.bitwave.ve3.core.scene.lights.SceneLights;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a scene in the game.
 */
public class Scene {
    private Camera camera;
    private Map<String, Model> modelMap;
    private Projection projection;
    private TextureCache textureCache;
    private SceneLights sceneLights;
    private Skybox skybox;

    public Scene(int width, int height) {
        modelMap = new HashMap<>();
        projection = new Projection(width, height);
        textureCache = new TextureCache();
        camera = new Camera();
        sceneLights = new SceneLights();
    }

    public void addEntity(Entity entity) {
        String modelId = entity.getModelId();
        Model model = modelMap.get(modelId);
        if (model == null) {
            throw new RuntimeException("Could not find model [" + modelId + "]");
        }
        model.getEntitiesList().add(entity);
    }

    public void addModel(Model model) {
        modelMap.put(model.getId(), model);
    }

    public void cleanup() {
        modelMap.values().forEach(Model::cleanup);
    }

    public SceneLights getSceneLights() {
        return sceneLights;
    }

    public Skybox getSkybox() {
        return skybox;
    }

    public Camera getCamera() {
        return camera;
    }

    public Map<String, Model> getModelMap() {
        return modelMap;
    }

    public Projection getProjection() {
        return projection;
    }

    public TextureCache getTextureCache() {
        return textureCache;
    }

    public void setSceneLights(SceneLights sceneLights) {
        this.sceneLights = sceneLights;
    }

    public void setSkybox(Skybox skybox) { this.skybox = skybox; }

    public void resize(int width, int height) {
        projection.updateMatrix(width, height);
    }
}
