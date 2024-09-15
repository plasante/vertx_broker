package com.uniksoft.broker;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBResponse {

  private final static Logger LOG = LoggerFactory.getLogger(DBResponse.class);

  public static void errorHandler(RoutingContext context, Throwable error, String message) {
    //LOG.error("Failure: ", error.getMessage());
    context.response()
      .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
      .end(new JsonObject()
        .put("message", message)
        .put("path", context.normalizedPath())
        .encodePrettily()
      );
  }

  public static void errorHandler2(RoutingContext context, String message) {
    context.response()
      .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
      .end(new JsonObject()
        .put("message", message)
        .put("path", context.normalizedPath())
        .encodePrettily()
      );
  }

  public static void notFound(RoutingContext routingContext, String message) {
    routingContext.response()
      .setStatusCode(HttpResponseStatus.NOT_FOUND.code())
      .end(new JsonObject()
        .put("message", message)
        .put("path", routingContext.normalizedPath())
        .toBuffer()
      );
  }
}
