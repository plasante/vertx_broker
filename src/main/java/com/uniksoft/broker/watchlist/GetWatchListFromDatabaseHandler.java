package com.uniksoft.broker.watchlist;

import com.uniksoft.broker.DBResponse;
import com.uniksoft.broker.quotes.GetQuotesFromDatabaseHandler;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.templates.SqlTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

public class GetWatchListFromDatabaseHandler implements Handler<RoutingContext> {

  private final Logger LOG = LoggerFactory.getLogger(GetWatchListFromDatabaseHandler.class);

  private final Pool db;

  public GetWatchListFromDatabaseHandler(final Pool db) {
    this.db = db;
  }

  @Override
  public void handle(RoutingContext context) {
    var accountId = WatchListRestApi.getAccountId(context);

    SqlTemplate.forQuery(db,
        "SELECT w.asset FROM broker.watchlist w WHERE w.account_id = #{accountId}")
      .mapTo(Row::toJson)
      .execute(Collections.singletonMap("accountId",accountId.toString()))
      .onFailure(error -> {
        LOG.error("Database query failed :" ,error);
        DBResponse.errorHandler2(context, "Failed to fetch watchlist for account_id: " + accountId);
      })
      .onSuccess(assets -> {
        if (!assets.iterator().hasNext()) {
          DBResponse.notFound(context,"watchlist for account_id: " + accountId + " is not available");
          return;
        }
        var response = new JsonArray();
        assets.forEach(response::add);
        LOG.info("Path {} responds with {}", context.normalizedPath(), response.encodePrettily());
        context.response()
          .putHeader("content-type", "application/json")
          .end(response.encodePrettily());
      });
  }
}
