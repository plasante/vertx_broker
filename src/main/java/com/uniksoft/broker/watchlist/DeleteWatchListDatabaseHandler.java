package com.uniksoft.broker.watchlist;

import com.uniksoft.broker.DBResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.templates.SqlTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

public class DeleteWatchListDatabaseHandler implements Handler<RoutingContext> {

  private final Pool db;
  private Logger LOG = LoggerFactory.getLogger(DeleteWatchListDatabaseHandler.class);

  public DeleteWatchListDatabaseHandler(Pool db) {
    this.db = db;
  }

  @Override
  public void handle(RoutingContext context) {
    var accountId = WatchListRestApi.getAccountId(context);

    SqlTemplate.forUpdate(db,
      "DELETE FROM broker.watchlist WHERE account_id=#{accountId}")
      .execute(Collections.singletonMap("accountId", accountId.toString()))
      .onFailure(error -> DBResponse.errorHandler2(context,"Failed to delete for accountId: " + accountId))
      .onSuccess(result -> {
        LOG.info("Deleted {} rows from watchlist for accountId{} ", result.rowCount(), accountId);
        context.response()
          .setStatusCode(HttpResponseStatus.NO_CONTENT.code())
          .end();
      });
  }
}
