package org.bitwave.ve3.demo;

import org.bitwave.ve3.core.scene.Scene;
import org.bitwave.ve3.core.scene.lights.*;
import org.joml.Vector3f;

/**
 * A full set of variables to configure lighting in-engine.
 * @apiNote To be hooked up to a UI system for lighting controls in-engine.
 */
public class LightControls {
    private float[] ambientColor;
    private float[] ambientFactor;
    private float[] dirConeX;
    private float[] dirConeY;
    private float[] dirConeZ;
    private float[] dirLightColor;
    private float[] dirLightIntensity;
    private float[] dirLightX;
    private float[] dirLightY;
    private float[] dirLightZ;
    private float[] pointLightColor;
    private float[] pointLightIntensity;
    private float[] pointLightX;
    private float[] pointLightY;
    private float[] pointLightZ;
    private float[] spotLightColor;
    private float[] spotLightCutoff;
    private float[] spotLightIntensity;
    private float[] spotLightX;
    private float[] spotLightY;
    private float[] spotLightZ;

    public LightControls(Scene scene) {
        SceneLights sceneLights = scene.getSceneLights();
        AmbientLight ambientLight = sceneLights.getAmbientLight();
        Vector3f color = ambientLight.getColor();

        ambientFactor = new float[]{ambientLight.getIntensity()};
        ambientColor = new float[]{color.x, color.y, color.z};

        PointLight pointLight = sceneLights.getPointLights().get(0);
        color = pointLight.getColor();
        Vector3f pos = pointLight.getPosition();
        pointLightColor = new float[]{color.x, color.y, color.z};
        pointLightX = new float[]{pos.x};
        pointLightY = new float[]{pos.y};
        pointLightZ = new float[]{pos.z};
        pointLightIntensity = new float[]{pointLight.getIntensity()};

        SpotLight spotLight = sceneLights.getSpotLights().get(0);
        pointLight = spotLight.getPointLight();
        color = pointLight.getColor();
        pos = pointLight.getPosition();
        spotLightColor = new float[]{color.x, color.y, color.z};
        spotLightX = new float[]{pos.x};
        spotLightY = new float[]{pos.y};
        spotLightZ = new float[]{pos.z};
        spotLightIntensity = new float[]{pointLight.getIntensity()};
        spotLightCutoff = new float[]{spotLight.getCutOffAngle()};
        Vector3f coneDirection = spotLight.getConeDirection();
        dirConeX = new float[]{coneDirection.x};
        dirConeY = new float[]{coneDirection.y};
        dirConeZ = new float[]{coneDirection.z};

        DirectLight dirLight = sceneLights.getDirectLight();
        color = dirLight.getColor();
        pos = dirLight.getDirection();
        dirLightColor = new float[]{color.x, color.y, color.z};
        dirLightX = new float[]{pos.x};
        dirLightY = new float[]{pos.y};
        dirLightZ = new float[]{pos.z};
        dirLightIntensity = new float[]{dirLight.getIntensity()};
    }
}
