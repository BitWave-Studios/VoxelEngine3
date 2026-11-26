package org.bitwave.ve3.audio;

public interface DeviceTracker {
    DeviceList currentDevices();

    void tick();

    void forceRefresh();
}
