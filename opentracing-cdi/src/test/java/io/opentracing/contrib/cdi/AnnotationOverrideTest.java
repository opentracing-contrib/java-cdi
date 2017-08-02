package io.opentracing.contrib.cdi;

import io.opentracing.contrib.cdi.test.*;
import io.opentracing.contrib.tracerresolver.TracerResolver;
import io.opentracing.mock.MockTracer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

/**
 * This test verifies that {@link Traced} can be used at the method level to override what was specified at the type level.
 */
@RunWith(Arquillian.class)
public class AnnotationOverrideTest extends BaseTest {
    MockTracer tracer = MockTracerResolver.INSTANCE;

    @Inject
    OnlyOneMethodTraced onlyOneMethodTraced;

    @Inject
    OnlyOneMethodNotTraced onlyOneMethodNotTraced;

    @Inject
    AnnotationOnMethodOnly annotationOnMethodOnly;

    @Deployment
    public static WebArchive archive() {
        return baseArchive()
                .addClass(OnlyOneMethodNotTraced.class)
                .addClass(OnlyOneMethodTraced.class)
                .addClass(AnnotationOnMethodOnly.class)
                .addClass(MockTracerResolver.class)
                .addAsServiceProvider(TracerResolver.class, MockTracerResolver.class)
                .addAsWebInfResource("META-INF/test-beans.xml", "beans.xml")
                .addAsLibraries(library("io.opentracing:opentracing-mock"))
                ;
    }

    @Test
    public void typeTracedMethodNot() {
        tracer.reset();
        assertEquals(0, tracer.finishedSpans().size());
        onlyOneMethodNotTraced.notTraced();
        assertEquals(0, tracer.finishedSpans().size());
    }

    @Test
    public void typeTracedMethodInherits() {
        tracer.reset();
        assertEquals(0, tracer.finishedSpans().size());
        onlyOneMethodNotTraced.traced();
        assertEquals(1, tracer.finishedSpans().size());
    }

    @Test
    public void typeNotTracedMethodIs() {
        tracer.reset();
        assertEquals(0, tracer.finishedSpans().size());
        onlyOneMethodTraced.traced();
        assertEquals(1, tracer.finishedSpans().size());
    }

    @Test
    public void typeNotTracedMethodInherits() {
        tracer.reset();
        assertEquals(0, tracer.finishedSpans().size());
        onlyOneMethodTraced.notTraced();
        assertEquals(0, tracer.finishedSpans().size());
    }

    @Test
    public void typeWithoutAnnotationMethodWith() {
        tracer.reset();
        assertEquals(0, tracer.finishedSpans().size());
        annotationOnMethodOnly.traced();
        assertEquals(1, tracer.finishedSpans().size());
    }

    @Test
    public void typeWithoutAnnotationMethodWithout() {
        tracer.reset();
        assertEquals(0, tracer.finishedSpans().size());
        annotationOnMethodOnly.notTraced();
        assertEquals(0, tracer.finishedSpans().size());
    }

}
