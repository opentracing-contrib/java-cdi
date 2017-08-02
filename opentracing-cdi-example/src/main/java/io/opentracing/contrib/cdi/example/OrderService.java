package io.opentracing.contrib.cdi.example;

import io.opentracing.SpanContext;
import io.opentracing.contrib.cdi.Traced;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.logging.Logger;

/**
 * This is a regular synchronous stateless EJB. It demonstrates how to get the span context for the span it's wrapped
 * on. This can be used to pass down the call chain, create child spans or add baggage items.
 */
@Traced
public class OrderService {
    private static final Logger log = Logger.getLogger(OrderService.class.getName());

    @Inject
    Event<OrderPlacementEvent> orderPlacementEvent;

    @Inject
    SpanContext spanContext;

    public void processOrderPlacement() {
        log.info("Placing order");
        orderPlacementEvent.fire(new OrderPlacementEvent(spanContext));
    }
}
