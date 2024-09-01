package com.uniksoft.broker.quotes;

import com.uniksoft.broker.assets.Asset;
import com.uniksoft.broker.assets.AssetsRestApi;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class QuotesRestApi {
  private static final Logger LOG = LogManager.getLogger(QuotesRestApi.class);

  public static void attach(Router parent) {
    final Map<String, Quote> cachedQuotes = new HashMap<>();
    AssetsRestApi.ASSETS.forEach(symbol ->
      cachedQuotes.put(symbol, initRandomQuote(symbol))
    );

    // Create an HTTP end point with path /quotes
    // :assets is a path parameter i.e. /quotes/AAPL
    // with .handler we get a routing context
    parent.get("/quotes/:assets").handler(routingContext -> {
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
    });
  }

  private static Quote initRandomQuote(String assetParam) {
    return Quote.builder()
      .asset(new Asset(assetParam))
      .volume(randomValue())
      .ask(randomValue())
      .bid(randomValue())
      .lastPrice(randomValue())
      .build();
  }

  private static BigDecimal randomValue() {
    return BigDecimal.valueOf(Math.random() * 100);
  }
}
