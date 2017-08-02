package io.opentracing.contrib.cdi.test;

import io.opentracing.contrib.cdi.Traced;

@Traced(false)
public class OnlyOneMethodTraced {
    @Traced
    public void traced() {
    }

    public void notTraced() {
    }
}
