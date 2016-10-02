package io.disc99.hogan

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Signifies that a public API is subject to incompatible changes, or even removal, in a future release.
 */
@Retention(RetentionPolicy.CLASS)
@Target([
    ElementType.ANNOTATION_TYPE,
    ElementType.CONSTRUCTOR,
    ElementType.FIELD,
    ElementType.METHOD,
    ElementType.TYPE
])
@Documented
@interface Beta {
}