package org.bitwave.ve3.bridge;

import org.bitwave.ve3.bridge.launcher.Launcher;
import org.bitwave.ve3.bridge.launcher.LauncherProvider;

import java.util.ServiceLoader;

public class Bridge {
    private static boolean INITIALIZED;

    private static Launcher LAUNCHER;

    public static Launcher getLauncher() {
        if (!INITIALIZED)
            synchronized (Bridge.class) {
                if (!INITIALIZED) {
                    LAUNCHER = createLauncher();
                    INITIALIZED = true;
                }
            }
        return LAUNCHER;
    }

    private static Launcher createLauncher() {
        for (LauncherProvider provider : ServiceLoader.<LauncherProvider>load(LauncherProvider.class)) {
            Launcher launcher = provider.createLauncher();
            if (launcher != null)
                return launcher;
        }
        return null;
    }
}
