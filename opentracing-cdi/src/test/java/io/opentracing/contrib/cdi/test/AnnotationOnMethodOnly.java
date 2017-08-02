package io.opentracing.contrib.cdi.test;

import io.opentracing.contrib.cdi.Traced;

public class AnnotationOnMethodOnly {
    @Traced
    public void traced() {
    }

    public void notTraced() {
    }
}
