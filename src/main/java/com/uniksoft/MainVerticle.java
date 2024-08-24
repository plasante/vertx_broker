package com.uniksoft;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

import io.vertx.core.Vertx;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class MainVerticle extends AbstractVerticle {

  private static final Logger LOG = LogManager.getLogger(MainVerticle.class);

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.exceptionHandler(error -> {
      LOG.error("Unhandled: {}", error);
    });
    vertx.deployVerticle(new MainVerticle(), ar -> {
      if (ar.failed()) {
        LOG.error("Deployment failed: {}", ar.cause());
        return;
      }
      LOG.info("Deployment successful: {}", ar.result());
    });
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.createHttpServer().requestHandler(req -> {
      req.response()
        .putHeader("content-type", "text/plain")
        .end("Hello from Vert.x!");
    }).listen(8889).onComplete(http -> {
      if (http.succeeded()) {
        startPromise.complete();
        LOG.info("Vert.x server started on port 8889");
      } else {
        startPromise.fail(http.cause());
      }
    });
  }
}
