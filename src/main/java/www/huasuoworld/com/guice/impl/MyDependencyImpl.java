package www.huasuoworld.com.guice.impl;


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
  public String hello() {
    return myDao.hello();
  }
}
