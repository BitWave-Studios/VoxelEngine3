package org.bitwave.ve3.core;

import org.bitwave.ve3.core.render.Render;
import org.bitwave.ve3.core.scene.Scene;
import org.bitwave.ve3.core.window.Window;

/**
 * Interface for the application logic that is used by the engine to run the application.
 * @author k4i
 */
public interface IAppLogic {
    void cleanup();
    void init(Window window, Scene scene, Render renderer);
    void input(Window window, Scene scene, long diffTimeMillis);
    void update(Window window, Scene scene, long diffTimeMillis);
}
