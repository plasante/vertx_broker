package com.uniksoft.broker;

import com.uniksoft.MainVerticle;
import com.uniksoft.broker.assets.AssetsRestApi;
import com.uniksoft.broker.quotes.QuotesRestApi;
import com.uniksoft.broker.watchlist.WatchListRestApi;
import com.uniksoft.httpHandlers.RequestMethodHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

import static com.uniksoft.MainVerticle.handleRouteFailure;

public class RestApiVerticle extends AbstractVerticle {

  private static final Logger LOG = LogManager.getLogger(RestApiVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    startHttpServerAndAttachRoutes(startPromise);
  }

  public void startHttpServerAndAttachRoutes(final Promise<Void> startPromise) throws Exception {
    Router restApi = Router.router(vertx);

    // register a failure on all routes
    restApi.route()
      .handler(BodyHandler.create())     // This will enable body handling for all routes
      .failureHandler(errorContext -> {
        handleRouteFailure(errorContext);
      });

    restApi.route("/favicon.ico").handler(this::handleFavicon);

    // Defining end points
    AssetsRestApi.attach(restApi);
    QuotesRestApi.attach(restApi);
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

  private void handleFavicon(RoutingContext routingContext) {
    // You could also serve an actual icon here if you prefer
    routingContext.response().end();
  }
}
