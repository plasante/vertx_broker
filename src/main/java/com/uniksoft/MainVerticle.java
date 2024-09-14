package com.uniksoft;

import com.uniksoft.broker.RestApiVerticle;
import com.uniksoft.broker.config.DbConfig;
import com.uniksoft.broker.db.migration.FlywayMigration;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


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
        .compose(next -> migrateDatabase())
          .onFailure(startPromise::fail)
          .onSuccess(id -> LOG.info("Migrated db schema to latest version!"))
        .onSuccess(id -> {
          LOG.info("RestApiVerticle deployed: {}", id);
          startPromise.complete();
        });
  }

  private Future<Void> migrateDatabase() {
    DbConfig dbConfig = DbConfig.builder().build();
    return FlywayMigration.migrate(vertx, dbConfig);
  }

  private static int numberOfProcessors() {
    return Math.max(1,Runtime.getRuntime().availableProcessors());
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
