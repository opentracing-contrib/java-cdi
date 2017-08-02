package io.opentracing.contrib.cdi.example;

import io.opentracing.SpanContext;

public class OrderPlacementEvent {
    private SpanContext spanContext;

    public OrderPlacementEvent(SpanContext spanContext) {
        this.spanContext = spanContext;
    }

    public SpanContext getSpanContext() {
        return spanContext;
    }
}
