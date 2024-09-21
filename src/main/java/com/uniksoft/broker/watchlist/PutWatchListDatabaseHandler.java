package com.uniksoft.broker.watchlist;

import com.uniksoft.broker.DBResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.templates.SqlTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PutWatchListDatabaseHandler implements Handler<RoutingContext> {

  private Logger LOG = LoggerFactory.getLogger(PutWatchListDatabaseHandler.class);
  private final Pool db;

  public PutWatchListDatabaseHandler(Pool db) {
    this.db = db;
  }

  public void handle(final Pool db) {

  }

  @Override
  public void handle(RoutingContext context) {
    var accountId = WatchListRestApi.getAccountId(context);

    var json = context.getBodyAsJson();
    var watchList = json.mapTo(WatchList.class);

    // inserting in a batch
    var parameterBatch = watchList.getAssets().stream()
      .map(asset -> {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("account_id", accountId.toString());
        parameters.put("asset", asset.getName());
        return parameters;
      }).collect(Collectors.toUnmodifiableList());

    // Only adding is possible
    SqlTemplate.forUpdate(db,
      "INSERT INTO broker.watchlist VALUES (#{account_id}, #{asset})"
       + " ON CONFLICT (account_id, asset) DO NOTHING")
      .executeBatch(parameterBatch)
      .onFailure(error -> {
        LOG.error("Database insert asset into watchlist failed :", error);
        DBResponse.errorHandler2(context, "Failed to insert into watchlist");
      })
      .onSuccess(result -> {
        LOG.info("Database insert asset into watchlist completed");
        context.response()
          .setStatusCode(HttpResponseStatus.NO_CONTENT.code())
          .end();
      });

    // inserting one by one is very inefficient
//    watchList.getAssets().forEach(asset -> {
//      final HashMap<String, Object> parameters = new HashMap<>();
//      parameters.put("account_id", accountId.toString());
//      parameters.put("asset", asset.getName());
//      SqlTemplate.forUpdate(db,
//        "INSERT INTO broker.watchlist VALUES (#{account_id}, #{asset})")
//        .execute(parameters)
//        .onFailure(error -> {
//          LOG.error("Database insert asset into watchlist failed :", error);
//          DBResponse.errorHandler2(context, "Failed to insert into watchlist");
//        })
//        .onSuccess(result -> {
//          if (!context.response().ended()) {
//            LOG.info("Database insert asset into watchlist completed");
//            context.response()
//              .setStatusCode(HttpResponseStatus.NO_CONTENT.code())
//              .end();
//          }
//        });
//    });
  }
}
