[![Build Status][ci-img]][ci] [![Released Version][maven-img]][maven]

# OpenTracing CDI Instrumentation

This library provides instrumentation for [Contexts and Dependency Injection (CDI)](https://jcp.org/en/jsr/detail?id=299)
modules in Java EE applications.

## Usage

For a concrete and comprehensive set of examples, refer to the `opentracing-cdi-example`
module.

### Injecting a tracer

In your managed beans, you can get an instance of the appropriate tracer via:

```java
@Inject
Tracer tracer;
```

If you have your own `Tracer` producer, you'll need to mark your own as an
`@Alternative`

```java
@Produces @Alternative
public Tracer getMyTracer() {
    return MyTracer.getInstance();
}
```

### Injecting a `SpanContext`

If you have the OpenTracing Servlet integration on the classpath and registered the filter,
it's possible to get the request's `SpanContext` via CDI:

```java
@Inject
SpanContext spanContext;
```

### Injecting a `Scope`

This integration also exposes a producer that allows the injection of a `Scope`:

```java
@Inject
Scope scope;
```

All the caveats of using the `Scope` apply. As a rule of thumb, it can be considered
safe to use on synchronous applications, but should not be used for async processing. You can
safely mix sync and async if you pass the context from the sync object to the async explicitly,
like:

```java
@Inject Scope scope

public void myAction() {
    myAsyncAction.perform(scpe.context());
}
```

### Tracer registration via  GlobalTracer

This integration also features a registration of the `Tracer` with the `GlobalTracer`,
so that other integrations have access to the `Tracer` producer from your own application.
For that to work, make sure that `@Inject Tracer tracer` injects your own tracer, probably
using CDI alternatives.

If there are no `Tracer` producers on the classpath, the registration will attempt to use
the `TracerResolver` and register its outcome. This means that, in the common scenario, you
should be able to just add a tracer implementation to the classpath and everything would work
as expected.

If you have your own registration procedure with the `TracerResolver`, you might want to skip
this by setting the system property `skipCdiTracerInitializer`, otherwise, you might get an
exception from the `GlobalTracer` when trying to register your Tracer, as this integration might
have registered one already.

### Interceptor

To use the interceptor, add a `beans.xml` to your deployment (`webapp/WEB-INF`
Maven WAR projects), like this:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://xmlns.jcp.org/xml/ns/javaee"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/beans_1_1.xsd"
       bean-discovery-mode="all">

    <interceptors>
        <class>io.opentracing.contrib.cdi.CdiOpenTracingInterceptor</class>
    </interceptors>

</beans>
```

Once the interceptor is in place, simply annotate your CDI beans with `@Traced`, like:

```java
@Traced
public class InventoryService {
    public String placeOrder() {
    }
}
```

or annotate a single method, like:

```java
public class InventoryService {
    @Traced
    public String placeOrder() {
    }
}
```

It's also possible to specify that a class should be traced, except for one method, like:

```java
@Traced
public class InventoryService {
    // this method is not traced
    @Traced(false)
    public String placeOrder() {
    }

    // this method is traced
    public String requestMoreFromSupplier() {
    }
}
```


## Development
```shell
./mvnw clean install
```

## Release
Follow instructions in [RELEASE](RELEASE.md)

   [ci-img]: https://travis-ci.org/opentracing-contrib/java-cdi.svg?branch=master
   [ci]: https://travis-ci.org/opentracing-contrib/java-cdi
   [maven-img]: https://img.shields.io/maven-central/v/io.opentracing.contrib/opentracing-cdi.svg?maxAge=2592000
   [maven]: http://search.maven.org/#search%7Cga%7C1%7Copentracing-cdi
