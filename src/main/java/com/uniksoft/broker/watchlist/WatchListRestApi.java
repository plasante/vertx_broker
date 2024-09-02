package com.uniksoft.broker.watchlist;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
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
    putWatchList(parent, path, watchListPerAccount);
    deleteWatchList(parent, path, watchListPerAccount);

  }

  private static void deleteWatchList(Router parent, String path, HashMap<UUID, WatchList> watchListPerAccount) {
    parent.delete(path).handler(context -> {
      var accountId = getAccountId(context);
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
    });
  }

  private static void putWatchList(Router parent, String path, HashMap<UUID, WatchList> watchListPerAccount) {
    parent.put(path).handler(context -> {
      var accountId = getAccountId(context);
      var json = context.getBodyAsJson();
      var watchList = json.mapTo(WatchList.class);
      //Todo: This could fail if UUID is invalid or the body of the request
      // is mal-formatted
      watchListPerAccount.put(UUID.fromString(String.valueOf(accountId)), watchList);
      // We return the response
      context.response().end(json.toBuffer());
    });
  }

  private static void getWatchList(Router parent, String path, HashMap<UUID, WatchList> watchListPerAccount) {
    parent.get(path).handler(context -> {
      var accountId = getAccountId(context);
      var watchList = watchListPerAccount.get(accountId);
      Optional.ofNullable(watchList)
        .ifPresentOrElse(
          accountWatchList -> context.response().end(accountWatchList.toJsonObject().encode()),
          () -> context.response()
            .setStatusCode(HttpResponseStatus.NOT_FOUND.code())
            .end(new JsonObject()
              .put("message", "watchlist for account " + accountId + " not available!")
              .put("path", context.normalizedPath())
              .toBuffer()
            ));
    });
  }

  private static UUID getAccountId(RoutingContext context) {
    var accountId = context.pathParam("accountId");
    LOG.debug("{} for account {}", context.normalizedPath(), accountId);
    return UUID.fromString(accountId); //Explicit conversion to UUID
  }
}
