package com.uniksoft.broker.quotes;

import com.uniksoft.broker.DBResponse;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.templates.SqlTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GetQuotesFromDatabaseHandler implements Handler<RoutingContext> {

  private static final Logger LOG = LoggerFactory.getLogger(GetQuotesFromDatabaseHandler.class);

  private final Pool db;

  public GetQuotesFromDatabaseHandler(final Pool db) {
    this.db = db;
  }

  @Override
  public void handle(final RoutingContext context) {
    final String assetParam = context.pathParam("assets");
    LOG.info("Received request for asset: {}", assetParam);

    SqlTemplate.forQuery(db,
      "SELECT q.asset, q.bid, q.ask, q.last_price, q.volume FROM broker.quotes q WHERE asset=#{assetParam}")
        .execute(Collections.singletonMap("assetParam", assetParam))
        .onFailure(error -> {
          LOG.error("Database request failed with error: {}", error.getMessage());
          DBResponse.errorHandler2(context, "Failed to get quote for asset " + assetParam + " in DB");
        })
        .onSuccess(result -> {
          if (result.size() == 0) {
            DBResponse.notFound(context, "No quotes found for asset " + assetParam);
            return;
          } else {
            List<QuoteEntity> quoteEntities = getQuoteEntities(result);
            var response = quoteEntities.iterator().next().toJsonObject();
            LOG.info("Successfully retrieved quotes {} for asset {}", response.encodePrettily(), assetParam);
            context.response()
              .putHeader("content-type", "application/json")
              .end(response.encodePrettily());
            };
        });
  }

  private static List<QuoteEntity> getQuoteEntities(RowSet<Row> result) {
    List<QuoteEntity> quoteList = new ArrayList<>();
    result.forEach(row -> {
      QuoteEntity quote = new QuoteEntity();
        quote.setAsset(row.getString("asset"));
        quote.setBid(row.getBigDecimal("bid"));
        quote.setAsk(row.getBigDecimal("ask"));
        quote.setLastPrice(row.getBigDecimal("last_price"));
        quote.setVolume(row.getBigDecimal("volume"));
        quoteList.add(quote);
      });
    return quoteList;
  }
}
