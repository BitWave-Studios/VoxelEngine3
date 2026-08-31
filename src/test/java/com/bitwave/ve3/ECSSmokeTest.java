package com.bitwave.ve3;

import com.bitwave.ve3.core.ecs.*;

public class ECSSmokeTest {

    record Position(float x, float y) implements Component {}
    record Velocity(float dx, float dy) implements Component {}

    public static void main(String[] args) {
        World world = new World();

        Entity a = world.spawn();
        world.add(a, new Position(0, 0));
        world.add(a, new Velocity(1, 1));

        Entity b = world.spawn();
        world.add(b, new Position(5, 5));

        world.query(Position.class, Velocity.class).forEach((entity, pos, vel) -> {
            System.out.println("Entity " + entity + " pos=" + pos + " vel=" + vel);
        });

        world.despawn(a);
        System.out.println("a alive after despawn: " + world.isAlive(a));

        Entity c = world.spawn();
        System.out.println("c: " + c + " (compare index to a: " + a + ")");
        System.out.println("a still considered alive (should be false): " + world.isAlive(a));
    }
}
