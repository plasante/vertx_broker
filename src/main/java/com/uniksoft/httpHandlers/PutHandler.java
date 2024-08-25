package com.uniksoft.httpHandlers;

import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PutHandler implements RequestMethodHandler {
  private static final Logger LOG = LogManager.getLogger(PutHandler.class);

  @Override
  public void handle(RoutingContext context, String path) {
    LOG.info("Put handler called");
    context.response().end("PUT Hello World");
  }
}
