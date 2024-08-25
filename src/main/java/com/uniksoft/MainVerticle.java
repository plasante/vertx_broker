package com.uniksoft;

import com.uniksoft.httpHandlers.*;
import io.vertx.core.Promise;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;


public class MainVerticle extends MainVerticleAbstract {

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
  public void start(Promise<Void> startPromise) {
    Router router = Router.router(vertx);
    router.route("/favicon.ico").handler(this::handleFavicon);


    Map<HttpMethod, RequestMethodHandler> strategies = getHttpMethodRequestMethodHandlerMap();

    router.route().handler(routingContext -> {
      HttpServerRequest request = routingContext.request();
      RequestMethodHandler handler = strategies.get(request.method());
        handler.handle(routingContext);
    });

    vertx.createHttpServer().requestHandler(router).listen(8888).onComplete(http -> {
      if (http.succeeded()) {
        startPromise.complete();
        LOG.info("Vert.x server started on port 8889");
      } else {
        startPromise.fail(http.cause());
      }
    });
  }
  private void handleFavicon(RoutingContext routingContext) {
    //LOG.info("Favicon requested");

    // You could also serve an actual icon here if you prefer
    routingContext.response().end();
  }

}
