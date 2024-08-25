package com.uniksoft.httpHandlers;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GetHandler implements RequestMethodHandler {
  private static final Logger LOG = LogManager.getLogger(GetHandler.class);

  @Override
  public void handle(RoutingContext context, String path) {
    LOG.info("Get handler called");
    LOG.info("Path: {} responds with {}", context.normalizedPath(), getAssetsArray().encodePrettily());
    context.response().end(getAssetsArray().encodePrettily());
  }

  private static JsonArray getAssetsArray() {
    final JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonObject().put("symbol", "AAPL"));
    jsonArray.add(new JsonObject().put("symbol", "AMZN"));
    jsonArray.add(new JsonObject().put("symbol", "NFLX"));
    jsonArray.add(new JsonObject().put("symbol", "TSLA"));
    return jsonArray;
  }
}
