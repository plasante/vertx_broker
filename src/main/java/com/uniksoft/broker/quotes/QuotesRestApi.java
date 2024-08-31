package com.uniksoft.broker.quotes;

import com.uniksoft.broker.assets.Asset;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;

public class QuotesRestApi {
  private static final Logger LOG = LogManager.getLogger(QuotesRestApi.class);

  public static void attach(Router parent) {

    // Create an HTTP end point with path /quotes
    // :assets is a path parameter i.e. /quotes/AAPL
    // with .handler we get a routing context
    parent.get("/quotes/:assets").handler(routingContext -> {
      final String assetParam = routingContext.pathParam("assets");
      LOG.info("Received request for asset: {}", assetParam);

      // We return a fictitious asset
      var quote = initRandomQuote(assetParam);

      final JsonObject response = quote.toJsonObject();
      LOG.info("Path {} responds with {}", routingContext.normalisedPath(), response.encodePrettily());
      routingContext.response().end(quote.toJsonObject().toBuffer());
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
