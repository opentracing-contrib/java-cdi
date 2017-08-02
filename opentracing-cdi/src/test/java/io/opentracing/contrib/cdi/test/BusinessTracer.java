package io.opentracing.contrib.cdi.test;

import io.opentracing.mock.MockTracer;

import javax.enterprise.inject.Alternative;

@Alternative
public class BusinessTracer extends MockTracer {
}
