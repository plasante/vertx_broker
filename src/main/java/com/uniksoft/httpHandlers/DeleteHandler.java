package com.uniksoft.httpHandlers;

import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DeleteHandler implements RequestMethodHandler {
  private static final Logger LOG = LogManager.getLogger(DeleteHandler.class);

  @Override
  public void handle(RoutingContext context) {
    LOG.info("Delete handler called");
    context.response().end("DELETE Hello World");
  }
}
