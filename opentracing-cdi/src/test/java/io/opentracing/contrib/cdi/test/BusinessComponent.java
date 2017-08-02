package io.opentracing.contrib.cdi.test;

import io.opentracing.contrib.cdi.Traced;

import java.util.logging.Logger;

@Traced
public class BusinessComponent {
    private static final Logger log = Logger.getLogger(BusinessComponent.class.getName());

    public void businessMethod() {
        log.info("Done some business stuff!");
    }
}
