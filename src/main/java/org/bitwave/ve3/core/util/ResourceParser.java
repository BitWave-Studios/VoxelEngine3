package org.bitwave.ve3.core.util;

import java.io.BufferedReader;

@FunctionalInterface
public interface ResourceParser<T> {
    T parse(BufferedReader paramBufferedReader) throws Exception;
}
