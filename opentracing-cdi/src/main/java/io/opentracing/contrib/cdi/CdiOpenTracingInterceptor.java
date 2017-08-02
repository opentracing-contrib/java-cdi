package io.opentracing.contrib.cdi;

import io.opentracing.contrib.ejb.OpenTracingInterceptor;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.lang.reflect.Method;

/**
 * This is a CDI Interceptor, wrapping the traced methods within an OpenTracing {@link io.opentracing.Span}, should
 * they be annotated with {@link Traced}. The interceptor looks at the type-level and method-level annotations and
 * determines whether the method call should be traced or not.
 *
 * The actual OpenTracing wrapping happens at the more generic {@link OpenTracingInterceptor} and sets the component
 * to "cdi".
 *
 * @see OpenTracingInterceptor
 */
@Traced
@Interceptor
public class CdiOpenTracingInterceptor extends OpenTracingInterceptor {
    @Override
    public String getComponent() {
        return "cdi";
    }

    @AroundInvoke
    public Object wrap(InvocationContext ctx) throws Exception {
        // first, let's find where this is annotated:
        Method method = ctx.getMethod();
        Class<?> resourceClass = method.getDeclaringClass();

        boolean trace = true;
        if (resourceClass.isAnnotationPresent(Traced.class)) {
            trace = resourceClass.getAnnotation(Traced.class).value();
        }

        if (method.isAnnotationPresent(Traced.class)) {
            trace = method.getAnnotation(Traced.class).value();
        }

        if (trace) {
            return super.wrap(ctx);
        } else {
            return ctx.proceed();
        }
    }
}
