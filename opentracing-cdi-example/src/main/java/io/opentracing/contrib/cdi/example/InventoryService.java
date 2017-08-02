package io.opentracing.contrib.cdi.example;

import io.opentracing.contrib.cdi.Traced;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.logging.Logger;

/**
 * This is an asynchronous stateless EJB with spans created automatically by the interceptor. Note that the span context
 * that this method sees is <b>not</b> the same as the span context sent by the caller: the interceptor wraps this
 * method call on its own span, and replaces the span context by the context of this new span. This is done so that this
 * span context can be passed along to the next service "as is".
 */
@Traced
public class InventoryService {
    private static final Logger log = Logger.getLogger(InventoryService.class.getName());

    @Inject
    Event<InventoryChangeEvent> inventoryChangeEvent;

    public void changeInventory(@Observes OrderPlacementEvent event) {
        log.info("Changing the inventory");
        inventoryChangeEvent.fire(new InventoryChangeEvent());
    }
}
