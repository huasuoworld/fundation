package www.huasuoworld.com.webapiservice.jdbc;

import io.vertx.core.Vertx;
import io.vertx.ext.jdbc.JDBCClient;

public interface MysqlConnection {
  public JDBCClient getClient();
  public void setVertx(Vertx vertx);
}
