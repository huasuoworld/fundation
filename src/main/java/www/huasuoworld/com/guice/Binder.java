package www.huasuoworld.com.guice;

import com.google.inject.AbstractModule;
import www.huasuoworld.com.dao.MyDao;
import www.huasuoworld.com.dao.impl.MyDaoImpl;
import www.huasuoworld.com.guice.impl.MyDependencyImpl;

import javax.inject.Singleton;

/**
 * Guice module
 */
public class Binder extends AbstractModule {
  /**
   * Configures a {@link Binder} via the exposed methods.
   */
  @Override
  protected void configure() {
    bind(MyDependency.class).to(MyDependencyImpl.class).in(Singleton.class);
    bind(MyDao.class).to(MyDaoImpl.class).in(Singleton.class);
  }
}
