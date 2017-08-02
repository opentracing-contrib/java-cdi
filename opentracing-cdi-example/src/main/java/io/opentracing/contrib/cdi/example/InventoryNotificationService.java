package io.opentracing.contrib.cdi.example;

import io.opentracing.ActiveSpan;
import io.opentracing.Span;
import io.opentracing.Tracer;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.logging.Logger;

/**
 * This is the final call in the chain. This is an asynchronous stateless EJB, which obtains the span context
 * via a method parameter. This bean is not intercepted in any way by us, so, the span context received is exactly
 * the same as what was sent by the caller.
 */
public class InventoryNotificationService {
    private static final Logger log = Logger.getLogger(InventoryNotificationService.class.getName());

    @Inject
    Tracer tracer;

    @Inject
    ActiveSpan parentSpan;

    public void sendNotification(@Observes InventoryChangeEvent event) {
        Span span = tracer.buildSpan("myBusinessSpan").asChildOf(parentSpan.context()).startManual();
        log.info("Sending an inventory change notification");
        span.finish();
    }

}
