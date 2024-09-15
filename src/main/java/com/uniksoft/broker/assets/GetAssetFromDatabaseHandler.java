package com.uniksoft.broker.assets;

import com.uniksoft.broker.DBResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Pool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetAssetFromDatabaseHandler implements Handler<RoutingContext> {
  private static final Logger LOG = LoggerFactory.getLogger(GetAssetFromDatabaseHandler.class);
  private final Pool db;

  public GetAssetFromDatabaseHandler(final Pool db) {
    this.db = db;
  }

  @Override
  public void handle(RoutingContext context) {

    db.query("SELECT a.value FROM broker.assets a")
      .execute()
      .onFailure(error -> {
        DBResponse.errorHandler(context, error, "Failed to get assets from database");
      })
      .onSuccess(result -> {
        var response = new JsonArray();
        result.forEach(row -> {
          response.add(row.getValue("value"));
        });
        LOG.info("Path {} responds with {}", context.normalizedPath(), response.encode());
        context.response()
          .putHeader("content-type", "application/json")
          .end(response.encodePrettily());
      })
    ;
  }
}
