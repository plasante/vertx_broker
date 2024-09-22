package com.uniksoft.broker.quotes;

import com.uniksoft.MainVerticle;
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
public class TestQuotesRestApi {
//  private final Logger LOG = LogManager.getLogger(TestQuotesRestApi.class);
//
//  @BeforeEach
//  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
//    vertx.deployVerticle(new MainVerticle()).onComplete(testContext.succeeding(id -> testContext.completeNow()));
//  }
//
//  @Test
//  void return_quote_for_asset(Vertx vertx, VertxTestContext testContext) throws Throwable {
//    // Create an asynchronous WebClient
//    // The default port is the port of our server
//    var client = WebClient.create(vertx,
//      new WebClientOptions().setDefaultPort(MainVerticle.PORT));
//    // GET asynchronous response
//    // .send() returns a Future
//    // We need to use testContext.succeeding() otherwise Junit will never be
//    // notified of the outcome of the test
//    // testContext.completeNow() est necessaire pour garantir que le test
//    // se termine correctement.
//    client.get("/quotes/AMZN")
//      .send()
//      .onComplete(testContext.succeeding(response -> {
//        var json = response.bodyAsJsonObject();
//        LOG.info("Response: {}", json);
//        var expectedEncode = "{name=AMZN}";
//        assertEquals(expectedEncode, json.getString("asset"));
//        assertEquals(200, response.statusCode());
//        testContext.completeNow();
//      }));
//  }
//
//  @Test
//  void return_not_found_for_unknown_asset(Vertx vertx, VertxTestContext testContext) throws Throwable {
//    var client = WebClient.create(vertx,
//      new WebClientOptions().setDefaultPort(MainVerticle.PORT));
//    client.get("/quotes/UNKNOWN")
//      .send()
//      .onComplete(testContext.succeeding(response -> {
//        var json = response.bodyAsJsonObject();
//        String expectedResponse = "{\"message\":\"quote for asset UNKNOWN not available!\",\"path\":\"/quotes/UNKNOWN\"}";
//        assertEquals(expectedResponse, json.encode());
//        assertEquals(404, response.statusCode());
//        testContext.completeNow();
//      }));
//  }
}
