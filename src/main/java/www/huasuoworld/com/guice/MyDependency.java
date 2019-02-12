package www.huasuoworld.com.guice;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;

/**
 * An injectable dependency
 */
public interface MyDependency {

  public String hello(RoutingContext routingContext);
}
