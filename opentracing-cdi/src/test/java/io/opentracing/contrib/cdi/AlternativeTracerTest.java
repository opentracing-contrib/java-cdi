package io.opentracing.contrib.cdi;

import io.opentracing.Tracer;
import io.opentracing.contrib.cdi.test.MockTracerResolver;
import io.opentracing.contrib.cdi.test.BusinessComponent;
import io.opentracing.contrib.cdi.test.BusinessTracer;
import io.opentracing.contrib.tracerresolver.TracerResolver;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertTrue;

/**
 * This test verifies that an alternative bean listed on beans.xml can override the Tracer provided by this integration's
 * producer.
 */
@RunWith(Arquillian.class)
public class AlternativeTracerTest extends BaseTest {
    @Inject
    Tracer tracer;

    @Deployment
    public static WebArchive archive() {
        return baseArchive().addClass(BusinessComponent.class)
                .addClass(BusinessTracer.class)
                .addClass(MockTracerResolver.class)
                .addAsServiceProvider(TracerResolver.class, MockTracerResolver.class)
                .addAsWebInfResource("META-INF/alternative-tracer-beans.xml", "beans.xml")
                .addAsLibraries(library("io.opentracing:opentracing-mock"))
                ;
    }

    @Test
    public void alternativeTracerGetsInjected() {
        assertTrue(tracer instanceof BusinessTracer);
    }
}
