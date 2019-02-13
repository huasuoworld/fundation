package www.huasuoworld.com.webapiservice.jdbc;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;

public class MysqlConnectionImpl implements MysqlConnection {

  private JDBCClient client;
  @Override
  public JDBCClient getConn(Vertx vertx) {
    if(client == null) {
      client = JDBCClient.createShared(vertx, new JsonObject()
        .put("url", "jdbc:mysql//localhost:3306/test?shutdown=true")
        .put("driver_class", "com.mysql.jdbc.Driver")
        .put("max_pool_size", 30)
        .put("user", "root")
        .put("password", "root"));
    }
    return client;
  }
}
