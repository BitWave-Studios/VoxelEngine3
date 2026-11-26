package org.bitwave.ve3.audio;

import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALUtil;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public record DeviceList(@Nullable String defaultDevice, List<String> allDevices) {
    public static final DeviceList EMPTY = new DeviceList(null, List.of());

    public static DeviceList query() {
        if (!ALC10.alcIsExtensionPresent(0L, "ALC_ENUMERATE_ALL_EXT")) {
            return EMPTY;
        }

        List<String> allDevices = Objects.requireNonNullElse(ALUtil.getStringList(0L, 4115), List.of());
        String defaultDevice = ALC10.alcGetString(0L, 4114);
        return new DeviceList(defaultDevice, allDevices);
    }
}
