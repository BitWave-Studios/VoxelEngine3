package org.bitwave.ve3.core.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated class or method is only available in the specified environment(s).
 * <pre><code>
 *     {@code @Environment({EnvironmentType.CLIENT})}
 *     public class ClientOnlyClass {
 *         // This class is only available in the client environment.
 *     }
 * </code></pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Environment {
    EnvironmentType[] value();
}
