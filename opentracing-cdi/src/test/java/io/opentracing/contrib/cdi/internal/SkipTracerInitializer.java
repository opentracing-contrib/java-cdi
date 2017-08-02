package io.opentracing.contrib.cdi.internal;

import io.opentracing.contrib.cdi.BaseTest;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Verifies that the initializer can be skipped by setting a system property
 */
public class SkipTracerInitializer extends BaseTest {
    @Test
    public void defaultsToNotSkip() {
        System.clearProperty(TracerInitializer.SKIP_PROPERTY);
        assertFalse(new TracerInitializer().skip());
    }

    @Test
    public void skipIfSpecified() {
        System.clearProperty(TracerInitializer.SKIP_PROPERTY);
        System.setProperty(TracerInitializer.SKIP_PROPERTY, "");
        assertTrue(new TracerInitializer().skip());
    }

    @Test
    public void skipIfSetToSomething() {
        System.clearProperty(TracerInitializer.SKIP_PROPERTY);
        System.setProperty(TracerInitializer.SKIP_PROPERTY, "true");
        assertTrue(new TracerInitializer().skip());

        System.setProperty(TracerInitializer.SKIP_PROPERTY, "TrUe");
        assertTrue(new TracerInitializer().skip());

        System.setProperty(TracerInitializer.SKIP_PROPERTY, "something");
        assertTrue(new TracerInitializer().skip());
    }

    @Test
    public void doNotSkipIfSetToFalse() {
        System.clearProperty(TracerInitializer.SKIP_PROPERTY);
        System.setProperty(TracerInitializer.SKIP_PROPERTY, "false");
        assertFalse(new TracerInitializer().skip());

        System.setProperty(TracerInitializer.SKIP_PROPERTY, "FALSE");
        assertFalse(new TracerInitializer().skip());

        System.setProperty(TracerInitializer.SKIP_PROPERTY, "FalSe");
        assertFalse(new TracerInitializer().skip());
    }
}
