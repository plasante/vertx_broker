package com.uniksoft.broker.watchlist;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.UUID;

public class DeleteWatchListHandler implements Handler<RoutingContext> {

  private static final Logger LOG = LoggerFactory.getLogger(DeleteWatchListHandler.class);

  private HashMap<UUID, WatchList> watchListPerAccount;

  public DeleteWatchListHandler(HashMap<UUID, WatchList> watchListPerAccount) {
    this.watchListPerAccount = watchListPerAccount;
  }

  @Override
  public void handle(RoutingContext context) {
    var accountId = WatchListRestApi.getAccountId(context);
    LOG.info("Entire HashMap: {}", watchListPerAccount);
    LOG.info("AccountId to Get: {}", accountId);
    final WatchList present = watchListPerAccount.get(accountId); // Here, we get the WatchList but don't delete yet
    if (present != null) {
      watchListPerAccount.remove(accountId); // Now we remove the WatchList
      context.response()
        .end(present.toJsonObject().encodePrettily());
    } else {
      context.response()
        .setStatusCode(HttpResponseStatus.NOT_FOUND.code())
        .end(new JsonObject().put("message", "No watchlist found for accountId: " + accountId).encodePrettily());
    }
  }
}
