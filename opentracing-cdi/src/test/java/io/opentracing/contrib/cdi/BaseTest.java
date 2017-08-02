package io.opentracing.contrib.cdi;

import io.opentracing.contrib.cdi.internal.TracerInitializer;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

import java.io.File;

/**
 * Base test, exposing a base archive with the common dependencies.
 */
public class BaseTest {
    public static WebArchive baseArchive() {
        return ShrinkWrap.create(WebArchive.class)
                .addClass(CdiOpenTracingInterceptor.class)
                .addPackage(Traced.class.getPackage())
                .addPackage(TracerInitializer.class.getPackage())
                .addAsLibraries(library("io.opentracing:opentracing-api"))
                .addAsLibraries(library("io.opentracing.contrib:opentracing-ejb"))
                .addAsLibraries(library("io.opentracing.contrib:opentracing-tracerresolver"))
                .addAsLibraries(library("io.opentracing:opentracing-util"))
                ;
    }

    static File[] library(String gav) {
        return Maven.resolver()
                .loadPomFromFile("../pom.xml")
                .resolve(gav)
                .withTransitivity()
                .as(File.class);
    }
}
