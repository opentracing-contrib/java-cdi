package io.opentracing.contrib.cdi.internal;

import io.opentracing.Tracer;
import io.opentracing.contrib.tracerresolver.TracerResolver;
import io.opentracing.util.GlobalTracer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.logging.Logger;

/**
 * Registers a {@link Tracer} with the {@link GlobalTracer}, should it be a tracer that is defined by the target
 * application. If no {@link Tracer} is specified by the target application and made available via
 * {@code @Inject Tracer tracer}, this initializer attempts to locate one via the {@link TracerResolver}, further setting
 * it to the {@link GlobalTracer}, so that a {@link Tracer} can be injected into components from the target application.
 *
 * An application-specific tracer is a {@link Tracer} that is produced by the application and not by this integration and
 * available via {@code @Inject Tracer tracer}.
 *
 * A tracer is initialized and registered with the {@link GlobalTracer} if there are no tracers registered with it already.
 *
 * This component can be disabled by setting the system property {@code skipCdiTracerInitializer} with any value except
 * "false" (case insensitive).
 */
public class TracerInitializer {
    static final String SKIP_PROPERTY = "skipCdiTracerInitializer";
    private static final Logger log = Logger.getLogger(TracerInitializer.class.getName());
    private Boolean skip = null;

    @Inject
    Instance<Tracer> tracerInstance;

    public void init(@Observes @Initialized(ApplicationScoped.class) Object ignored) {
        // 1) check if there's a Tracer that can be injected
        // 1.1) if we have one, and if it's *not* our own producer returning a GlobalTracer, then it's a user's configured
        //      and we'll just use it
        // 2) check if GlobalTracer already has a Tracer registered
        // 3) use the TracerResolver to get a Tracer
        // 4) register this with the GlobalTracer

        if (skip()) {
            return;
        }

        if (GlobalTracer.isRegistered()) {
            log.info("A Tracer is already registered at the GlobalTracer. Skipping resolution.");
            return;
        }

        if (!tracerInstance.isUnsatisfied()) {
            // the Tracer is already available!
            Tracer tracer = tracerInstance.get();

            if (!(tracer instanceof GlobalTracer)) {
                // it's not our own producer, which returns a GlobalTracer. We'll just use it then!
                GlobalTracer.register(tracer);
                log.info(String.format("A Tracer is already available as bean: %s", tracerInstance.get().getClass().getName()));
                return;
            }
        }

        Tracer tracer = TracerResolver.resolveTracer();
        if (null == tracer) {
            log.info("Could not get a valid OpenTracing Tracer from the classpath. Skipping.");
            return;
        }

        log.info(String.format("Registering %s as the OpenTracing Tracer", tracer.getClass().getName()));
        GlobalTracer.register(tracer);
    }

    boolean skip() {
        if (null != this.skip) {
            return skip;
        }

        String skipProperty = System.getProperty(SKIP_PROPERTY);
        if (null != skipProperty) {
            // -DskipProperty=FALSE won't skip, -DskipProperty will
            skip = !("false".equalsIgnoreCase(skipProperty));
        } else {
            // do not skip
            skip = false;
        }

        return skip;
    }
}
