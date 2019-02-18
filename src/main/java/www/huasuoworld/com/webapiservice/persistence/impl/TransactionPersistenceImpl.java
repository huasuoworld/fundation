package www.huasuoworld.com.webapiservice.persistence.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
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

  @Inject
  public TransactionPersistenceImpl(SQLClient jdbc) {
    this.jdbc = jdbc;
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
              System.out.println("transactionId..." + transactionId + "..." + val.toString());
              resultHandler.handle(Future.succeededFuture(OperationResponse.completedWithJson(val)));
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
    });
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
    JsonArray json = new JsonArray("[\"4cb3596fadbe4e74ac23d90efb18c3bd\",\"items\",\"thomas@example.com\",\"francesco@example.com\",46.0]");
    System.out.println(json.toString());
  }
}
