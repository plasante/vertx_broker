package com.uniksoft.broker.watchlist;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Pool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.UUID;

public class WatchListRestApi {

  private static final Logger LOG = LoggerFactory.getLogger(WatchListRestApi.class);

  public static void attach(final Router parent, Pool db) {
    // We use InMemory WatchList
    final HashMap<UUID, WatchList> watchListPerAccount = new HashMap<>();

    String path = "/account/watchlist/:accountId";
    getWatchList(parent, path, watchListPerAccount);
    putWatchList(parent, path, watchListPerAccount);
    deleteWatchList(parent, path, watchListPerAccount);

    String pgPath = "/pg/account/watchlist/:accountId";
    parent.get(pgPath).handler(new GetWatchListFromDatabaseHandler(db));
    parent.put(pgPath).handler(new PutWatchListDatabaseHandler(db));
  }

  private static void deleteWatchList(Router parent, String path, HashMap<UUID, WatchList> watchListPerAccount) {
    parent.delete(path).handler(new DeleteWatchListHandler(watchListPerAccount));
  }

  private static void putWatchList(Router parent, String path, HashMap<UUID, WatchList> watchListPerAccount) {
    parent.put(path).handler(new PutWatchListHandler(watchListPerAccount));
  }

  private static void getWatchList(Router parent, String path, HashMap<UUID, WatchList> watchListPerAccount) {
    parent.get(path).handler(new GetWatchListHandler(watchListPerAccount));
  }

  static UUID getAccountId(RoutingContext context) {
    var accountId = context.pathParam("accountId");
    LOG.debug("{} for account {}", context.normalizedPath(), accountId);
    return UUID.fromString(accountId); //Explicit conversion to UUID
  }
}
