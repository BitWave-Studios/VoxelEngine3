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
