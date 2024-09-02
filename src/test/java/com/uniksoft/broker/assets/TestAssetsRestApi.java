package com.uniksoft.broker.assets;

import com.uniksoft.MainVerticle;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(VertxExtension.class)
public class TestAssetsRestApi {

  private final Logger LOG = LogManager.getLogger(TestAssetsRestApi.class);

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle()).onComplete(testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void return_all_assets(Vertx vertx, VertxTestContext testContext) throws Throwable {
    // Create an asynchronous WebClient
    // The default port is the port of our server
    var client = WebClient.create(vertx,
      new WebClientOptions().setDefaultPort(MainVerticle.PORT));
    // GET asynchronous response
    // .send() returns a Future
    // We need to use testContext.succeeding() otherwise Junit will never be
    // notified of the outcome of the test
    // testContext.completeNow() est necessaire pour garantir que le test
    // se termine correctement.
    client.get("/assets")
      .send()
      .onComplete(testContext.succeeding(response -> {
        var json = response.bodyAsJsonArray();
        LOG.info("Response: {}", json);
        var expectedEncode = "[{\"name\":\"AAPL\"},{\"name\":\"AMZN\"},{\"name\":\"FB\"},{\"name\":\"GOOG\"},{\"name\":\"MSFT\"},{\"name\":\"TSLA\"}]";
        assertEquals(expectedEncode, json.encode());
        assertEquals(200, response.statusCode());
        assertEquals(HttpHeaderValues.APPLICATION_JSON.toString(),
          response.getHeader("Content-Type"));
        testContext.completeNow();
      }));
  }
}
