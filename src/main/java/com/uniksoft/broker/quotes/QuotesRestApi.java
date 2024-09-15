package com.uniksoft.broker.quotes;

import com.uniksoft.broker.assets.Asset;
import com.uniksoft.broker.assets.AssetsRestApi;
import io.vertx.ext.web.Router;
import io.vertx.sqlclient.Pool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class QuotesRestApi {
  private static final Logger LOG = LogManager.getLogger(QuotesRestApi.class);

  public static void attach(Router parent, final Pool db) {
    final Map<String, Quote> cachedQuotes = new HashMap<>();
    AssetsRestApi.ASSETS.forEach(symbol ->
      cachedQuotes.put(symbol, initRandomQuote(symbol))
    );

    // Create an HTTP end point with path /quotes
    // :assets is a path parameter i.e. /quotes/AAPL
    // with .handler we get a routing context
    parent.get("/quotes/:assets").handler(new GetQuotesHandler(cachedQuotes));
    parent.get("/pg/quotes/:assets").handler(new GetQuotesFromDatabaseHandler(db));
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
