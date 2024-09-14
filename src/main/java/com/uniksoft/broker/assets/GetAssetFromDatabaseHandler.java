package com.uniksoft.broker.assets;

import com.fasterxml.jackson.core.Base64Variant;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.pgclient.PgPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetAssetFromDatabaseHandler implements Handler<RoutingContext> {
  private static final Logger LOG = LoggerFactory.getLogger(GetAssetFromDatabaseHandler.class);
  private final PgPool db;

  public GetAssetFromDatabaseHandler(final PgPool db) {
    this.db = db;
  }

  @Override
  public void handle(RoutingContext context) {

    db.query("SELECT a.value FROM broker.assets a")
      .execute()
      .onFailure(error -> {
        LOG.error("Failure: ", error.getMessage());
        context.response()
          .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
          .end(new JsonObject()
            .put("message", "Failed to get assets from db!")
            .put("path", context.normalizedPath())
            .encodePrettily()
          );
      }).onSuccess(result -> {
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
