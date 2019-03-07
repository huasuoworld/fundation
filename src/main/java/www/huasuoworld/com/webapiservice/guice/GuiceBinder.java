package www.huasuoworld.com.webapiservice.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.sql.SQLClient;
import www.huasuoworld.com.webapiservice.persistence.TransactionPersistence;
import www.huasuoworld.com.webapiservice.persistence.impl.TransactionPersistenceImpl;
import www.huasuoworld.com.webapiservice.services.TransactionsManagerService;
import www.huasuoworld.com.webapiservice.services.impl.TransactionsManagerServiceImpl;

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
  @Singleton
  SQLClient jdbc() {
    System.out.println("jdbc init......");
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

  @Provides
  @Singleton
  JWTAuth provider() {
    JWTAuth provider = JWTAuth.create(vertx, new JWTAuthOptions()
      .addPubSecKey(new PubSecKeyOptions()
        .setAlgorithm("ES256")
        .setPublicKey(
          "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEraVJ8CpkrwTPRCPluUDdwC6b8+m4\n" +
            "dEjwl8s+Sn0GULko+H95fsTREQ1A2soCFHS4wV3/23Nebq9omY3KuK9DKw==\n")
        .setSecretKey(
          "MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQgeRyEfU1NSHPTCuC9\n" +
            "rwLZMukaWCH2Fk6q5w+XBYrKtLihRANCAAStpUnwKmSvBM9EI+W5QN3ALpvz6bh0\n" +
            "SPCXyz5KfQZQuSj4f3l+xNERDUDaygIUdLjBXf/bc15ur2iZjcq4r0Mr")
      ));
    return provider;
  }
}
