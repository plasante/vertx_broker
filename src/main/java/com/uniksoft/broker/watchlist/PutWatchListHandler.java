package com.uniksoft.broker.watchlist;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

import java.util.HashMap;
import java.util.UUID;

public class PutWatchListHandler implements Handler<RoutingContext> {

  private HashMap<UUID, WatchList> watchListPerAccount;

  public PutWatchListHandler(HashMap<UUID, WatchList> watchListPerAccount) {
    this.watchListPerAccount = watchListPerAccount;
  }

  @Override
  public void handle(RoutingContext context) {
    var accountId = WatchListRestApi.getAccountId(context);
    var json = context.getBodyAsJson();
    var watchList = json.mapTo(WatchList.class);
    //Todo: This could fail if UUID is invalid or the body of the request
    // is mal-formatted
    watchListPerAccount.put(UUID.fromString(String.valueOf(accountId)), watchList);
    // We return the response
    context.response().end(json.toBuffer());
  }
}
