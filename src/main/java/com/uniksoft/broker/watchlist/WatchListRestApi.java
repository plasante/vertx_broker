package com.uniksoft.broker.watchlist;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class WatchListRestApi {

  private static final Logger LOG = LoggerFactory.getLogger(WatchListRestApi.class);

  public static void attach(final Router parent) {
    // We use InMemory WatchList
    final HashMap<UUID, WatchList> watchListPerAccount = new HashMap<>();

    String path = "/account/watchlist/:accountId";
    getWatchList(parent, path, watchListPerAccount);
    parent.put(path).handler(context -> {

    });
    parent.delete(path).handler(context -> {

    });
  }

  private static void getWatchList(Router parent, String path, HashMap<UUID, WatchList> watchListPerAccount) {
    parent.get(path).handler(context -> {
      var accountId = context.pathParam("accountId");
      LOG.debug("{} for account {}", context.normalizedPath(), accountId);
      var watchList = watchListPerAccount.get(UUID.fromString(accountId));
      Optional.ofNullable(watchList)
        .ifPresentOrElse(
          wl -> context.response().end(wl.toJsonObject().toBuffer()),
          () -> context.response()
            .setStatusCode(HttpResponseStatus.NOT_FOUND.code())
            .end(new JsonObject()
              .put("message", "watchlist for account " + accountId + " not available!")
              .put("path", context.normalizedPath())
              .toBuffer()
            ));
    });
  }
}
