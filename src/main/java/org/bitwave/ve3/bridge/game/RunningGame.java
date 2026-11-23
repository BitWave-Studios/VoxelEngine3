package org.bitwave.ve3.bridge.game;

import org.bitwave.ve3.bridge.launcher.SessionEventListener;

public interface RunningGame {
    GameVersion getVersion();

    Language getSelectedLanguage();

    GameSession getCurrentSession();

    PerformanceMetrics getPerformanceMetrics();

    void setSessionEventListener(SessionEventListener paramSessionEventListener);
}
