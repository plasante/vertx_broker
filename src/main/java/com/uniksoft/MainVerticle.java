package com.uniksoft;

import com.uniksoft.broker.RestApiVerticle;
import com.uniksoft.broker.assets.AssetsRestApi;
import com.uniksoft.broker.quotes.QuotesRestApi;
import com.uniksoft.broker.watchlist.WatchListRestApi;
import com.uniksoft.httpHandlers.*;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;


public class MainVerticle extends MainVerticleAbstract {

  private static final Logger LOG = LogManager.getLogger(MainVerticle.class);
  public static final int PORT = 8888;

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.exceptionHandler(error -> {
      LOG.error("Unhandled: {}", error);
    });
    vertx.deployVerticle(new MainVerticle())
      .onFailure(err -> LOG.error("Failed to deploy: ", err))
      .onSuccess(id -> {
        LOG.info("MainVerticle deployed: {}", id);
      });
  }

  @Override
  public void start(Promise<Void> startPromise) {
    vertx.deployVerticle(RestApiVerticle.class.getName(),
        new DeploymentOptions().setInstances(numberOfProcessors()))
        .onFailure(startPromise::fail)
        .onSuccess(id -> {
          LOG.info("RestApiVerticle deployed: {}", id);
          startPromise.complete();
        });
  }

  private static int numberOfProcessors() {
    return Math.max(1,Runtime.getRuntime().availableProcessors() / 2);
  }

  public static void handleRouteFailure(RoutingContext errorContext) {
    if (errorContext.response().ended()) {
      // We ignore completed response
    } else {
      LOG.error("Route Error: {}", errorContext.failure());
      errorContext.response()
        .setStatusCode(500)
        .end(new JsonObject().put("message", "Something went wrong").encodePrettily());
    }
  }

  private void handleFavicon(RoutingContext routingContext) {
    // You could also serve an actual icon here if you prefer
    routingContext.response().end();
  }

}
