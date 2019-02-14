package www.huasuoworld.com.webapiservice.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.sql.SQLClient;
import www.huasuoworld.com.webapiservice.persistence.TransactionPersistence;
import www.huasuoworld.com.webapiservice.persistence.impl.TransactionPersistenceImpl;
import www.huasuoworld.com.webapiservice.services.TransactionsManagerService;
import www.huasuoworld.com.webapiservice.services.impl.TransactionsManagerServiceImpl;

import javax.inject.Singleton;

/**
 * Guice module
 */
public class GuiceBinder extends AbstractModule {

  private Vertx vertx;
  public GuiceBinder(Vertx vertx) {
    this.vertx = vertx;
  }
  /**
   * Configures a {@link www.huasuoworld.com.webapiservice.guice.GuiceBinder} via the exposed methods.
   */
  @Override
  protected void configure() {
    bind(TransactionPersistence.class).to(TransactionPersistenceImpl.class).in(Singleton.class);
    bind(TransactionsManagerService.class).to(TransactionsManagerServiceImpl.class).in(Singleton.class);
  }

  /***
   *{
   *  "host" : <your-host>,
   *  "port" : <your-port>,
   *  "maxPoolSize" : <maximum-number-of-open-connections>,
   *  "username" : <your-username>,
   *  "password" : <your-password>,
   *  "database" : <name-of-your-database>,
   *  "charset" : <name-of-the-character-set>,
   *  "connectTimeout" : <timeout-in-milliseconds>,
   *  "testTimeout" : <timeout-in-milliseconds>,
   *  "queryTimeout" : <timeout-in-milliseconds>,
   *  "maxConnectionRetries" : <maximum-number-of-connection-retries>,
   *  "connectionRetryDelay" : <delay-in-milliseconds>,
   *  "sslMode" : <"disable"|"prefer"|"require"|"verify-ca"|"verify-full">,
   *  "sslRootCert" : <path to file with certificate>
   * }
   * */
  @Provides
  SQLClient jdbc() {
    SQLClient jdbc = MySQLClient.createShared(vertx, new JsonObject()
      .put("host","10.60.1.22")
      .put("port", 3306)
      .put("maxPoolSize", 300)
      .put("connectTimeout", 30000)
      .put("charset", "UTF-8")
      .put("database", "xcloud")
      .put("username", "root")
      .put("password", "root"));

    return jdbc;
  }
}
