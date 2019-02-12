package www.huasuoworld.com.handler;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import www.huasuoworld.com.guice.MyDependency;

import javax.inject.Inject;

public class MyHandler implements Handler<RoutingContext> {

  private MyDependency myDependency;

  @Inject
  public MyHandler(MyDependency myDependency) {
    this.myDependency = myDependency;
  }

  @Override
  public void handle(RoutingContext routingContext) {
    routingContext.response().end(myDependency.hello(routingContext));
  }
}
