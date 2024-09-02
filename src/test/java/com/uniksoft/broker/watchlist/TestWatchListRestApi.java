package com.uniksoft.broker.watchlist;

import com.uniksoft.MainVerticle;
import com.uniksoft.broker.assets.Asset;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(VertxExtension.class)
public class TestWatchListRestApi {
  private final Logger LOG = LogManager.getLogger(TestWatchListRestApi.class);

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle()).onComplete(testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void adds_and_returns_watchlist_for_account(Vertx vertx, VertxTestContext testContext) throws Throwable {
    var client = WebClient.create(vertx,
      new WebClientOptions().setDefaultPort(MainVerticle.PORT));
    var accountId = UUID.randomUUID();
    client.put("/account/watchlist/" + accountId.toString())
      .sendJsonObject(getBody())
      .onComplete(testContext.succeeding(response -> {
        var json = response.bodyAsJsonObject();
        LOG.info("Response: {}", json);
        var expectedEncode = "{\"assets\":[{\"name\":\"AMZN\"},{\"name\":\"TLSA\"}]}";
        assertEquals(expectedEncode, json.encode());
        assertEquals(200, response.statusCode());
        testContext.completeNow();
      }));
  }

  private static JsonObject getBody() {
    return new WatchList(Arrays.asList(
      new Asset("AMZN"),
      new Asset("TLSA"))
    ).toJsonObject();
  }

}
