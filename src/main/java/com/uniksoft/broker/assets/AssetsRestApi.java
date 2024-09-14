package com.uniksoft.broker.assets;

import io.vertx.ext.web.Router;
import io.vertx.pgclient.PgPool;

import java.util.Arrays;
import java.util.List;

public class AssetsRestApi {
  public static final List<String> ASSETS = Arrays.asList("AAPL", "AMZN", "FB", "GOOG", "MSFT", "TSLA");

  public static void attach(Router parent, final PgPool db) {
    parent.get("/assets").handler(new GetAssetsHandler());
    parent.get("/pg/assets").handler(new GetAssetFromDatabaseHandler(db));
  }
}
