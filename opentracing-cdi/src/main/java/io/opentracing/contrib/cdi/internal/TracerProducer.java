package io.opentracing.contrib.cdi.internal;

import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;

import javax.enterprise.inject.Produces;

/**
 * Producer for {@link Tracer}. Simply delegates to {@link GlobalTracer}, so, whatever it returns as the {@link Tracer}
 * is what we are returning as well.
 *
 * Applications needing to set their own producers can do so by either specifying a {@link javax.enterprise.inject.Alternative}
 * via {@code beans.xml}, or by creating a {@link javax.inject.Qualifier} and using it both at the producing method
 * and at the injection points.
 */
public class TracerProducer {

    @Produces
    public Tracer produceTracer() {
        return GlobalTracer.get();
    }
}
