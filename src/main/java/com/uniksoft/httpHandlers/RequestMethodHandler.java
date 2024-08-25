package com.uniksoft.httpHandlers;

import io.vertx.ext.web.RoutingContext;

public interface RequestMethodHandler {
  void handle(RoutingContext context);
}
