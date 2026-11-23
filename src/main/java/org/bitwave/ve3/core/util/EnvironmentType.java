package org.bitwave.ve3.core.util;

public enum EnvironmentType {
    CLIENT("client"),
    SERVER("server");

    private final String description;

    EnvironmentType(final String description) {
        this.description = description;
    }

    public static EnvironmentType from(VEServer server) {
        return server.isDedicatedServer() ? SERVER : CLIENT;
    }

    public String getDescription() {
        return description;
    }
}
