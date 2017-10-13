package io.opentracing.contrib.cdi.test;

import io.opentracing.Tracer;
import io.opentracing.contrib.tracerresolver.TracerResolver;
import io.opentracing.mock.MockTracer;
import io.opentracing.util.ThreadLocalScopeManager;

public class MockTracerResolver extends TracerResolver {
    public static final MockTracer INSTANCE = new MockTracer(new ThreadLocalScopeManager());

    @Override
    protected Tracer resolve() {
        return INSTANCE;
    }
}
