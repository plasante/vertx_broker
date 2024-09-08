package com.uniksoft.broker.assets;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.concurrent.ThreadLocalRandom;

public class GetAssetsHandler implements Handler<RoutingContext> {
  private static final Logger LOG = LoggerFactory.getLogger(GetAssetsHandler.class);

  @Override
  public void handle(RoutingContext context) {
      final JsonArray response = new JsonArray();
      AssetsRestApi.ASSETS.stream().map(Asset::new).forEach(response::add);
      LOG.info("Path {} responds with {}", context.normalizedPath(), response.encode());
      // Let's introduce an artificial delay
    //artificialSleep(context);
    context.response()
          .putHeader(HttpHeaderNames.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString())
          .putHeader("my-header", "my-header-value")
          .end(response.encode());
  }

  /**
   * Only for testing purpose
   * @param context
   */
  private static void artificialSleep(RoutingContext context) {
    try {
      //Thread.sleep(200);
      int random = ThreadLocalRandom.current().nextInt(100, 300);
      if (random % 2 == 0) {
        Thread.sleep(random);
        context.response()
          .setStatusCode(500)
          .end("Sleeping...");
      }
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
