package org.bitwave.ve3.core.util;

/**
 * Simple sneaky exception thrower
 * @param <T>
 */
class Thrower<T extends Throwable> {
    private static final Thrower<RuntimeException> INSTANCE = new Thrower();

    public void sneakyThrow(Throwable exception) throws T {
        throw (T) exception;
    }
}
