package io.vertx.starter;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.vertx.core.AbstractVerticle;
import www.huasuoworld.com.guice.Binder;
import www.huasuoworld.com.guice.MyDependency;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start() {
    Injector injector = Guice.createInjector(new Binder());
    MyDependency MyDependency = injector.getInstance(MyDependency.class);
    vertx.createHttpServer()
        .requestHandler(req -> req.response().end(MyDependency.hello()))
        .listen(8080);
  }

}
