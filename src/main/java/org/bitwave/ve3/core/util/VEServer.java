package org.bitwave.ve3.core.util;


@Environment(EnvironmentType.SERVER)
public abstract class VEServer {

    private static final String VANILLA_BRAND = "vanilla"; // This is for servers that do not have plugins/addons
    private static final float AVERAGE_TICK_TIME_SMOOTHING = 0.8f;



    public abstract boolean isDedicatedServer();
    public abstract int getRateLimitPacketsPerSecond();
    public abstract int getCommandSpamThresholdSeconds();
    public abstract int getChatSpamThresholdSeconds();
    public abstract boolean isPublished();
    public abstract boolean shouldInformAdmins();
    // public abstract boolean isSingleplayerOwner(NameAndId paramNameAndId); // Temporarily removed until future implementation

}
