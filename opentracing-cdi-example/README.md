# OpenTracing CDI - Example

## The example

This example shows the different ways to use the OpenTracing CDI integration. It features
getting a `Tracer` injected into your components, as well as a `SpanContext` (when the Servlet
integration is also in place) and an `ActiveSpan` for non-async applications.

The example has the following call tree:

![Trace example](call-tree.png)

Refer to each individual example for more information on how it works.

## Running

In order to visualize the spans, you'll need an instance of Jaeger running locally.
Any other OpenTracing tracer is supported: all it requires is to change the `pom.xml` 
to remove Jaeger's dependencies and add your `TracerResolver` compatible Tracer.

Jaeger can be run via Docker as follows:
```
docker run \
    --rm \
    -p5775:5775/udp \
    -p6831:6831/udp \
    -p6832:6832/udp \
    -p5778:5778 \
    -p16686:16686 \
    -p14268:14268 \
    --name=jaeger \
    jaegertracing/all-in-one:latest
```

Make sure to at least export the environment variable `JAEGER_SERVICE_NAME`, 
otherwise Jaeger will complain. A good set for development and testing purposes
is the following:

```
export JAEGER_SERVICE_NAME=opentracing-cdi-example
export JAEGER_REPORTER_LOG_SPANS=true 
export JAEGER_SAMPLER_TYPE=const
export JAEGER_SAMPLER_PARAM=1 
```

Once that is done, the example can be run as:
```
mvn wildfly:run
```

After Wildfly starts, traces can be created by making calls like this:
```
curl -v -X POST localhost:8080/opentracing-cdi-example/v1/order
```

If everything works as expected, the following can be seen on the logs:
```
17:20:46,872 INFO  [io.opentracing.contrib.cdi.example.Endpoint] (default task-3) Request received to place an order
17:20:46,873 INFO  [io.opentracing.contrib.cdi.example.AccountService] (default task-3) Notifying the account owner about a new order
17:20:46,875 INFO  [com.uber.jaeger.reporters.LoggingReporter] (default task-3) Span reported: 2f6a5c42b102e71c:4df1c0323d99dee1:84dcd48f0b0f70c3:1 - sendNotification
17:20:46,876 INFO  [io.opentracing.contrib.cdi.example.OrderService] (default task-3) Placing order
17:20:46,877 INFO  [io.opentracing.contrib.cdi.example.InventoryService] (default task-3) Changing the inventory
17:20:46,879 INFO  [io.opentracing.contrib.cdi.example.InventoryNotificationService] (default task-3) Sending an inventory change notification
17:20:46,880 INFO  [com.uber.jaeger.reporters.LoggingReporter] (default task-3) Span reported: 2f6a5c42b102e71c:976d01584434fed0:6b753e96c583f66:1 - myBusinessSpan
17:20:46,882 INFO  [com.uber.jaeger.reporters.LoggingReporter] (default task-3) Span reported: 2f6a5c42b102e71c:6b753e96c583f66:36b15f4c7f7c9313:1 - changeInventory
17:20:46,884 INFO  [com.uber.jaeger.reporters.LoggingReporter] (default task-3) Span reported: 2f6a5c42b102e71c:36b15f4c7f7c9313:84dcd48f0b0f70c3:1 - processOrderPlacement
17:20:46,885 INFO  [com.uber.jaeger.reporters.LoggingReporter] (default task-3) Span reported: 2f6a5c42b102e71c:84dcd48f0b0f70c3:2f6a5c42b102e71c:1 - placeOrder
17:20:46,888 INFO  [com.uber.jaeger.reporters.LoggingReporter] (default task-3) Span reported: 2f6a5c42b102e71c:2f6a5c42b102e71c:0:1 - POST
```
