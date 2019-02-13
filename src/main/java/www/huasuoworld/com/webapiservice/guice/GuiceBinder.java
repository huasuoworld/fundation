package www.huasuoworld.com.webapiservice.guice;

import com.google.inject.AbstractModule;
import www.huasuoworld.com.webapiservice.persistence.TransactionPersistence;
import www.huasuoworld.com.webapiservice.persistence.impl.TransactionPersistenceImpl;
import www.huasuoworld.com.webapiservice.services.TransactionsManagerService;
import www.huasuoworld.com.webapiservice.services.impl.TransactionsManagerServiceImpl;

import javax.inject.Singleton;

/**
 * Guice module
 */
public class GuiceBinder extends AbstractModule {
  /**
   * Configures a {@link www.huasuoworld.com.guice.Binder} via the exposed methods.
   */
  @Override
  protected void configure() {
    bind(TransactionPersistence.class).to(TransactionPersistenceImpl.class).in(Singleton.class);
    bind(TransactionsManagerService.class).to(TransactionsManagerServiceImpl.class).in(Singleton.class);
  }
}
