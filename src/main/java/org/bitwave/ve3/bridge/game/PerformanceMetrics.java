package org.bitwave.ve3.bridge.game;


public interface PerformanceMetrics {
    int getMinTime();

    int getMaxTime();

    int getAverageTime();

    int getSampleCount();
}
