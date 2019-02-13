package io.vertx.starter;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import www.huasuoworld.com.guice.Binder;
import www.huasuoworld.com.guice.MyDependency;
import www.huasuoworld.com.handler.MyHandler;

public class MainVerticle extends AbstractVerticle {

  //local runner
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle());
  }

  @Override
  public void start() {
    Injector injector = Guice.createInjector(new Binder());

    Router router = Router.router(vertx);

    router.route().handler(BodyHandler.create());
    router.get("/hello").handler(new MyHandler(injector.getInstance(MyDependency.class)));

    vertx.createHttpServer().requestHandler(router).listen(8081);
  }

}
