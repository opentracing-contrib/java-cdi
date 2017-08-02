package io.opentracing.contrib.cdi;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specifies whether or not the type/method should be traced. When used at the type, all non-annotated methods will
 * inherit its configuration. When used at the method, the annotation on the type (if any) is ignored.
 *
 * When used at the type, marks all methods on the type for tracing by the interceptor. When used at the method,
 * marks the method for tracing by the interceptor.
 */
@Inherited
@InterceptorBinding
@Retention(RUNTIME)
@Target({METHOD, TYPE})
public @interface Traced {
    boolean value() default true;
}
