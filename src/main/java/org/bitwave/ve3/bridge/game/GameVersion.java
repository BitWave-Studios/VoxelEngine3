package org.bitwave.ve3.bridge.game;


import java.util.Date;

public interface GameVersion {
    String getId();

    String getName();

    default String getSeriesId() {
        return "main";
    }

    int getWorldVersion();

    int getProtocolVersion();

    int getPackVersion(PackType paramPackType);

    Date getBuildTime();

    boolean isStable();
}
