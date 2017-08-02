package io.opentracing.contrib.cdi;

import io.opentracing.Tracer;
import io.opentracing.contrib.cdi.test.*;
import io.opentracing.contrib.tracerresolver.TracerResolver;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertTrue;

/**
 * This test verifies that a qualified tracer can be injected without interference from the integration's Tracer producer
 */
@RunWith(Arquillian.class)
public class QualifiedTracerTest extends BaseTest {
    @Inject @SuperTracer
    Tracer tracer;

    @Deployment
    public static WebArchive archive() {
        return baseArchive().addClass(BusinessComponent.class)
                .addClass(BusinessTracer.class)
                .addClass(QualifiedTracer.class)
                .addClass(MockTracerResolver.class)
                .addClass(SuperTracer.class)
                .addAsServiceProvider(TracerResolver.class, MockTracerResolver.class)
                .addAsLibraries(library("io.opentracing:opentracing-mock"))
                // having an alternative doesn't matter, as we have a qualified bean
                .addAsWebInfResource("META-INF/alternative-tracer-beans.xml", "beans.xml")
                ;
    }

    @Test
    public void qualifiedTracerGetsInjected() {
        assertTrue(tracer instanceof QualifiedTracer);
    }
}
