package com.uniksoft.httpHandlers;

import com.uniksoft.broker.assets.Asset;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GetHandler implements RequestMethodHandler {
  private static final Logger LOG = LogManager.getLogger(GetHandler.class);

  @Override
  public void handle(RoutingContext context, String path) {
    try {
      LOG.info("Get handler called");
      LOG.info("Path: {} responds with {}", context.normalizedPath(), getAssetsArray().encodePrettily());
      context.response().end(getAssetsArray().encodePrettily());
    } catch (Exception e) {
      LOG.error("Unexpected error {}", e);
      context.fail(500);
    }
  }

  private static JsonArray getAssetsArray() {
    final JsonArray jsonArray = new JsonArray();
    jsonArray.add(new Asset("GOOG"));
    jsonArray.add(new Asset("AAPL"));
    jsonArray.add(new Asset("AMZN"));
    jsonArray.add(new Asset("NFLX"));
    jsonArray.add(new Asset("TSLA"));
    return jsonArray;
  }
}
