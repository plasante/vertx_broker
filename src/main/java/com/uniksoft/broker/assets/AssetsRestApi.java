package com.uniksoft.broker.assets;

import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;

public class AssetsRestApi {
  private static final Logger LOG = LoggerFactory.getLogger(AssetsRestApi.class);
  //public static final List<String> ASSETS = new ArrayList<>();
  public static final List<String> ASSETS = Arrays.asList("AAPL", "AMZN", "FB", "GOOG", "MSFT", "TSLA");

  public static void attach(Router parent) {
    parent.get("/assets").handler(context -> {
      final JsonArray response = new JsonArray();
//      response
//        .add(new Asset("AAPL"))
//        .add(new Asset("AMZN"))
//        .add(new Asset("NFLX"))
//        .add(new Asset("TSLA"))
      ASSETS.stream().map(Asset::new).forEach(response::add);
        LOG.info("Path {} responds with {}", context.normalizedPath(), response.encode());
      context.response()
        .putHeader(HttpHeaderNames.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString())
        .putHeader("my-header", "my-header-value")
        .end(response.encode());
    });
  }
}
