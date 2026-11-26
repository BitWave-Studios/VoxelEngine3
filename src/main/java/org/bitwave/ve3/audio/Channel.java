package org.bitwave.ve3.audio;


import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;

public class Channel {
    private static final int QUEUED_BUFFER_COUNT = 4;
    public static final int BUFFER_DURATION_SECONDS = 1;
    private final int source;
    private final AtomicBoolean initialized = new AtomicBoolean(true);
    private int streamingBufferSize = 16384;
    private @Nullable AudioStream stream;
}
