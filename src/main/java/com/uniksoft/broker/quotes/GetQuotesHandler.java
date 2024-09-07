package com.uniksoft.broker.quotes;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

public class GetQuotesHandler implements Handler<RoutingContext> {
  private static final Logger LOG = LoggerFactory.getLogger(GetQuotesHandler.class);
  private final Map<String, Quote> cachedQuotes;

  public GetQuotesHandler(Map<String, Quote> cachedQuotes) {
    this.cachedQuotes = cachedQuotes;
  }

  @Override
  public void handle(RoutingContext routingContext) {
    final String assetParam = routingContext.pathParam("assets");
    LOG.info("Received request for asset: {}", assetParam);

    // We return a fictitious asset
    // It is possible that assetParam (i.e. AMZN) is not found
    var maybeQuote = Optional.ofNullable(cachedQuotes.get(assetParam));
    if (maybeQuote.isEmpty()) {
      routingContext.response()
        .setStatusCode(HttpResponseStatus.NOT_FOUND.code())
        .end(new JsonObject()
          .put("message", "quote for asset " + assetParam + " not available!")
          .put("path", routingContext.normalizedPath())
          .toBuffer()
        );
      return;
    }

    final JsonObject response = maybeQuote.get().toJsonObject();
    LOG.info("Path {} responds with {}", routingContext.normalisedPath(), response.encodePrettily());
    routingContext.response().end(response.toBuffer());
  }
}
