package org.bitwave.ve3.core.util;

public enum Environment {
    CLIENT("client"),
    SERVER("server");

    private final String description;

    Environment(final String description) {
        this.description = description;
    }

    public static Environment from(VEServer server) {
        return server.isDedicatedServer() ? SERVER : CLIENT;
    }

    public String getDescription() {
        return description;
    }
}
