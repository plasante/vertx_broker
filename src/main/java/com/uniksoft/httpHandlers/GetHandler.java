package com.uniksoft.httpHandlers;

import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GetHandler implements RequestMethodHandler {
  private static final Logger LOG = LogManager.getLogger(GetHandler.class);

  @Override
  public void handle(RoutingContext context) {
    LOG.info("Get handler called");
    context.response().end("GET Hello World");
  }
}
