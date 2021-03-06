package www.huasuoworld.com.webapiservice.persistence.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.jwt.JWTOptions;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.SQLRowStream;
import io.vertx.ext.web.api.OperationResponse;
import org.apache.commons.lang3.StringUtils;
import www.huasuoworld.com.webapiservice.models.Transaction;
import www.huasuoworld.com.webapiservice.persistence.TransactionPersistence;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * In memory implementation of transaction persistence
 *
 * @author Francesco Guardiani @slinkydeveloper
 */
public class TransactionPersistenceImpl implements TransactionPersistence {

  private Map<String, Transaction> transactions;
  private SQLClient jdbc;
  private JWTAuth provider;

  @Inject
  public TransactionPersistenceImpl(SQLClient jdbc, JWTAuth provider) {
    this.jdbc = jdbc;
    this.provider = provider;
    this.transactions = new HashMap<>();
  }

  @Override
  public List<Transaction> getFilteredTransactions(Predicate<Transaction> p) {
    return transactions.values().stream().filter(p).collect(Collectors.toList());
  }

  @Override
  public void getTransaction(String transactionId, Handler<AsyncResult<OperationResponse>> resultHandler) {
//    Map<String, JsonObject> transactionMap = new HashMap<>();

//    jdbc.queryWithParams("select * from transaction where id = ?", new JsonArray().add(transactionId), rs -> {
////      Transaction transaction = null;
//      JsonArray val = null;
//      if (rs.failed()) {
//        System.err.println("Cannot retrieve the data from the database");
//        rs.cause().printStackTrace();
//        return;
//      }
//
//      for (JsonArray line : rs.result().getResults()) {
//          val = line;
////        transaction = new Transaction(line.getJsonObject(0));
////        transactions.put(transactionId, transaction);
//        System.out.println(line.encode());
//      }
//
//      // and close the connection
//      jdbc.close(done -> {
//        if (done.failed()) {
//          throw new RuntimeException(done.cause());
//        }
//      });
//      resultHandler.handle(Future.succeededFuture(OperationResponse.completedWithJson(val)));
//    });
//    resultHandler.handle(Future.succeededFuture(OperationResponse.completedWithJson(new JsonObject("{\"4cb3596fadbe4e74ac23d90efb18c3bd\",\"items\",\"thomas@example.com\",\"francesco@example.com\",467.0}"))));
    jdbc.getConnection(conn -> {
      if (conn.failed()) {
        System.err.println(conn.cause().getMessage());
        return;
      }
      final SQLConnection connection = conn.result();
      connection.queryStreamWithParams("select * from transaction where id = ?", new JsonArray().add(transactionId), stream -> {
        if (stream.failed()) {
          System.err.println("Cannot retrieve the data from the database");
          stream.cause().printStackTrace();
          return;
        }

        SQLRowStream sqlRowStream = stream.result();
        sqlRowStream.handler(row -> {
            if (row == null || StringUtils.isEmpty(row.encode())) {
              System.out.println("row is null");
              resultHandler.handle(Future.succeededFuture(new OperationResponse().setStatusCode(404).setStatusMessage("Not Found")));
            } else {
              // do something with the row...
              JsonArray val = new JsonArray(row.encode());
              Transaction t = new Transaction(val.getString(0), val.getString(1), val.getString(2), val.getString(3), val.getDouble(4));
              Optional<Transaction> o =  Optional.ofNullable(t);
              System.out.println("transactionId..." + transactionId + "..." + o.get().toJson());
              String token = provider.generateToken(new JsonObject(), new JWTOptions().setAlgorithm("ES256"));
              resultHandler.handle(Future.succeededFuture(OperationResponse.completedWithJson(o.get().toJson()).putHeader("token", token)));
            }
          }).endHandler(v -> {
            // no more data available, close the connection
            connection.close(done -> {
              System.out.println("relese......");
              if (done.failed()) {
                throw new RuntimeException(done.cause());
              }
            });
          });
      });
    }).close();
  }

  @Override
  public Transaction addTransaction(Transaction t) {
    transactions.put(t.getId(), t);
    jdbc.getConnection(conn -> {
      if (conn.failed()) {
        System.err.println(conn.cause().getMessage());
        return;
      }
      final SQLConnection connection = conn.result();
      connection.execute(t.insertSql(), insert -> {
        connection.close(done -> {
          if (done.failed()) {
            throw new RuntimeException(done.cause());
          }
        });
      });
    });
    return t;
  }

  @Override
  public boolean removeTransaction(String transactionId) {

    jdbc.getConnection(conn -> {
      if (conn.failed()) {
        System.err.println(conn.cause().getMessage());
        return;
      }
      final SQLConnection connection = conn.result();
      connection.execute("delete from transaction where id = " + transactionId, delete -> {
        connection.close(done -> {
          if (done.failed()) {
            throw new RuntimeException(done.cause());
          }
        });
      });
    });
    Transaction t = transactions.remove(transactionId);
    if (t != null) return true;
    else return false;
  }

  @Override
  public boolean updateTransaction(String transactionId, Transaction transaction) {
    Transaction t = transactions.replace(transactionId, transaction);
    if (t != null) return true;
    else return false;
  }

  public static void main(String[] args) {
    JsonObject json = new JsonObject("[\"4cb3596fadbe4e74ac23d90efb18c3bd\",\"items\",\"thomas@example.com\",\"francesco@example.com\",46.0]");
//    Transaction transaction = new Transaction();
    System.out.println(json.toString());
  }
}
