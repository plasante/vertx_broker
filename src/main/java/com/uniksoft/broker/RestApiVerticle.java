package com.uniksoft.broker;

import com.uniksoft.MainVerticle;
import com.uniksoft.broker.assets.AssetsRestApi;
import com.uniksoft.broker.quotes.QuotesRestApi;
import com.uniksoft.broker.watchlist.WatchListRestApi;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.PoolOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.uniksoft.MainVerticle.handleRouteFailure;

public class RestApiVerticle extends AbstractVerticle {

  private static final Logger LOG = LogManager.getLogger(RestApiVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    startHttpServerAndAttachRoutes(startPromise);
  }

  public void startHttpServerAndAttachRoutes(final Promise<Void> startPromise) throws Exception {

    // One pool for each Rest Api Verticle
    final Pool db = createDbPool();

    Router restApi = Router.router(vertx);

    // register a failure on all routes
    restApi.route()
      .handler(BodyHandler.create())     // This will enable body handling for all routes
      .failureHandler(errorContext -> {
        handleRouteFailure(errorContext);
      });

    restApi.route("/favicon.ico").handler(this::handleFavicon);

    // Defining end points
    AssetsRestApi.attach(restApi, db);
    QuotesRestApi.attach(restApi, db);
    WatchListRestApi.attach(restApi);

    vertx.createHttpServer()
      .requestHandler(restApi)
      .exceptionHandler(error -> LOG.error("HTTP Server error: {}", error))
      .listen(MainVerticle.PORT, http -> {
        if (http.succeeded()) {
          startPromise.complete();
          LOG.info("Vert.x server started on port {}", MainVerticle.PORT);
        } else {
          startPromise.fail(http.cause());
        }
      });
  }

  private PgPool createDbPool() {
    // Create DB Pool
    final var pgConnectOptions = new PgConnectOptions()
      .setHost("localhost")
      .setPort(5432)
      .setDatabase("vertx-share-broker")
      .setUser("postgres")
      .setPassword("secret");

    var poolOptions = new PoolOptions()
      .setMaxSize(4);

    final PgPool db = PgPool.pool(vertx, pgConnectOptions, poolOptions);
    return db;
  }

  private void handleFavicon(RoutingContext routingContext) {
    // You could also serve an actual icon here if you prefer
    routingContext.response().end();
  }
}
