package www.huasuoworld.com.guice.impl;


import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;
import www.huasuoworld.com.dao.MyDao;
import www.huasuoworld.com.guice.MyDependency;

import javax.inject.Inject;

/**
 * Default implementation of {@link MyDependency}
 */
public class MyDependencyImpl implements MyDependency {

  private MyDao myDao;
  @Inject
  public MyDependencyImpl(MyDao myDao) {
    this.myDao = myDao;
  }
  @Override
  public String hello(RoutingContext routingContext) {
    HttpServerRequest req = routingContext.request();
    System.out.println(req.path());
    MultiMap mm = req.params();
    System.out.println(mm.toString());
    return myDao.hello() + "...路径是..." + req.path();
  }
}
