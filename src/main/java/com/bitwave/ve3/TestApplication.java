package com.bitwave.ve3;

import com.bitwave.ve3.core.ecs.*;
import com.bitwave.ve3.platform.Application;
import com.bitwave.ve3.platform.Window;

import static org.lwjgl.opengl.GL11.*;

/**
 * Testing class for running an Application with the VE3 platform.
 */
public class TestApplication implements Application {
    private World world;
    private Scheduler scheduler;

    record Position(float x, float y) implements Component {}
    record Velocity(float dx, float dy) implements Component {}

    static class MovementSystem implements ECSSystem {
        @Override
        public void update(World world, float delta) {
            world.query(Position.class, Velocity.class).forEach((entity, pos, vel) -> {
                Position updated = new Position(
                        pos.x() + vel.dx() * delta,
                        pos.y() + vel.dy() * delta
                );
                world.add(entity, updated);
            });
        }
    }

    private static final String VERTEX_SRC = """
    #version 330 core
    layout(location = 0) in vec3 aPos;
    uniform mat4 uModel;
    uniform mat4 uView;
    uniform mat4 uProjection;
    void main() {
        gl_Position = uProjection * uView * uModel * vec4(aPos, 1.0);
    }
    """;

    private static final String FRAGMENT_SRC = """
    #version 330 core
    out vec4 FragColor;
    uniform vec3 uColor;
    void main() {
        FragColor = vec4(uColor, 1.0);
    }
    """;

    @Override
    public void init(Window window) {
        world = new World();
        scheduler = new Scheduler();
        scheduler.add(new MovementSystem());

        Entity e = world.spawn();
        world.add(e, new Position(0, 0));
        world.add(e, new Velocity(1, 0));

        glClearColor(0.1f, 0.1f, 0.15f, 1.0f);
    }

    @Override
    public void fixedUpdate(float fixedDelta) {
        scheduler.tick(world, fixedDelta);
    }

    @Override
    public void render(float alpha) {
        glClear(GL_COLOR_BUFFER_BIT);
        // TODO: Real render handling (later)
    }

    @Override
    public void shutdown() {
        // No cleanup tasks yet
    }
}
