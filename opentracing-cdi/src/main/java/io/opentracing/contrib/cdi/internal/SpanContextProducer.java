package io.opentracing.contrib.cdi.internal;

import io.opentracing.ActiveSpan;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.contrib.web.servlet.filter.TracingFilter;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

/**
 * Produces a {@link SpanContext}, based on the {@link HttpServletRequest}'s attributes.
 */
public class SpanContextProducer {
    private static final Logger log = Logger.getLogger(SpanContextProducer.class.getName());

    @Inject
    HttpServletRequest request;

    @Inject
    Tracer tracer;

    @Produces
    public SpanContext produceSpanContext() {
        SpanContext spanContext = (SpanContext) request.getAttribute(TracingFilter.SERVER_SPAN_CONTEXT);
        if (null == spanContext) {
            log.warning("A SpanContext has been requested, but none could be found on the HTTP request's " +
                    "attributes. Make sure the Servlet integration is on the classpath!");
        }

        return spanContext;
    }

    @Produces
    public ActiveSpan produceActiveSpan(InjectionPoint ip) {
        ActiveSpan activeSpan = tracer.activeSpan();
        if (null == activeSpan) {
            String spanName = ip.getMember().getName();
            return tracer.buildSpan(spanName).startActive();
        }

        return activeSpan;
    }
}
