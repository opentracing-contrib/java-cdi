package io.opentracing.contrib.cdi;

import io.opentracing.contrib.cdi.test.MockTracerResolver;
import io.opentracing.contrib.cdi.test.BusinessComponent;
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
 * Verifies that the CDI interceptor is able to intercept a business component annotated with {@link Traced}
 */
@RunWith(Arquillian.class)
public class CdiOpenTracingInterceptorTest extends BaseTest {
    MockTracer tracer = MockTracerResolver.INSTANCE;

    @Inject
    BusinessComponent businessComponent;

    @Deployment
    public static WebArchive archive() {
        return baseArchive().addClass(BusinessComponent.class)
                .addClass(MockTracerResolver.class)
                .addAsServiceProvider(TracerResolver.class, MockTracerResolver.class)
                .addAsWebInfResource("META-INF/test-beans.xml", "beans.xml")
                .addAsLibraries(library("io.opentracing:opentracing-mock"))
                ;
    }

    @Test
    public void canInterceptBean() {
        tracer.reset();
        assertEquals(0, tracer.finishedSpans().size());
        businessComponent.businessMethod();
        assertEquals(1, tracer.finishedSpans().size());
    }
}
