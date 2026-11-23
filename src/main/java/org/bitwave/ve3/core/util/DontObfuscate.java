package org.bitwave.ve3.core.util;

import javax.annotation.meta.TypeQualifierDefault;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Indicates that the annotated class or method should not be obfuscated during the build process.
 * @apiNote This is a marker annotation and does not have any effect on its own. This may be used to avoid
 * obfuscation on some internal libraries or specific classes that are kept open source.
 */
@TypeQualifierDefault({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface DontObfuscate {
}
