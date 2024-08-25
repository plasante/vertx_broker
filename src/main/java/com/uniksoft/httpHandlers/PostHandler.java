package com.uniksoft.httpHandlers;

import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PostHandler implements RequestMethodHandler {
  private static final Logger LOG = LogManager.getLogger(PostHandler.class);

  @Override
  public void handle(RoutingContext context) {
    LOG.info("Post handler called");
    context.response().end("POST Hello World");
  }
}
