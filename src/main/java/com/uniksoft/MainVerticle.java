package com.uniksoft;

import com.uniksoft.broker.assets.AssetsRestApi;
import com.uniksoft.broker.quotes.QuotesRestApi;
import com.uniksoft.broker.watchlist.WatchListRestApi;
import com.uniksoft.httpHandlers.*;
import io.vertx.core.Promise;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
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
    vertx.deployVerticle(new MainVerticle(), ar -> {
      if (ar.failed()) {
        LOG.error("Deployment failed: {}", ar.cause());
        return;
      }
      LOG.info("Deployment successful: {}", ar.result());
    });
  }

  @Override
  public void start(Promise<Void> startPromise) {
    Router restApi = Router.router(vertx);

    // register a failure on all routes
    restApi.route()
      .handler(BodyHandler.create())     // This will enable body handling for all routes
      .failureHandler(errorContext -> {
        handleRouteFailure(errorContext);
      });

    restApi.route("/favicon.ico").handler(this::handleFavicon);


    Map<HttpMethod, RequestMethodHandler> strategies = getHttpMethodRequestMethodHandlerMap();

    // Defining end points
    AssetsRestApi.attach(restApi);
    QuotesRestApi.attach(restApi);
    WatchListRestApi.attach(restApi);

//    restApi.route().handler(routingContext -> {
//      HttpServerRequest request = routingContext.request();
//      String path = request.path();
//      // handlerStrategy will depend on GET PUT POST DELETE
//      RequestMethodHandler handlerStrategy = strategies.get(request.method());
//      handlerStrategy.handle(routingContext, path);
//    });


    vertx.createHttpServer()
      .requestHandler(restApi)
      .exceptionHandler(error -> LOG.error("HTTP Server error: {}", error))
      .listen(PORT, http -> {
        if (http.succeeded()) {
          startPromise.complete();
          LOG.info("Vert.x server started on port {}", PORT);
        } else {
          startPromise.fail(http.cause());
        }
      });
  }

  private static void handleRouteFailure(RoutingContext errorContext) {
    if (errorContext.response().ended()) {
      // We ignore completed response
      return;
    } else {
      LOG.error("Route Error: {}", errorContext.failure());
      errorContext.response()
        .setStatusCode(500)
        .end(new JsonObject().put("message", "Something went wrong").encodePrettily());
    }
  }

  private void handleFavicon(RoutingContext routingContext) {
    //LOG.info("Favicon requested");

    // You could also serve an actual icon here if you prefer
    routingContext.response().end();
  }

}
