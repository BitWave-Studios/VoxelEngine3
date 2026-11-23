package org.bitwave.ve3.bridge.launcher;


import org.bitwave.ve3.bridge.game.GameSession;

public interface SessionEventListener {
    public static final SessionEventListener NONE = new SessionEventListener() {

    };

    default void onStartGameSession(GameSession session) {}

    default void onLeaveGameSession(GameSession session) {}
}
