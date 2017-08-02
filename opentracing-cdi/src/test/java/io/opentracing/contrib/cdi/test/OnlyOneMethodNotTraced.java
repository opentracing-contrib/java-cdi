package io.opentracing.contrib.cdi.test;

import io.opentracing.contrib.cdi.Traced;

@Traced
public class OnlyOneMethodNotTraced {
    public void traced() {
    }

    @Traced(false)
    public void notTraced() {
    }
}
