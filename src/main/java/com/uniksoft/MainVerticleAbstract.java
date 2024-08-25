package com.uniksoft;

import com.uniksoft.httpHandlers.*;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class MainVerticleAbstract extends AbstractVerticle {
  private static final Logger LOG = LogManager.getLogger(MainVerticleAbstract.class);

  protected static Map<HttpMethod, RequestMethodHandler> getHttpMethodRequestMethodHandlerMap() {
    Map<HttpMethod, RequestMethodHandler> strategies = new HashMap<>();
    strategies.put(HttpMethod.GET, new GetHandler());
    strategies.put(HttpMethod.POST, new PostHandler());
    strategies.put(HttpMethod.PUT, new PutHandler());
    strategies.put(HttpMethod.DELETE, new DeleteHandler());
    return strategies;
  }

}
