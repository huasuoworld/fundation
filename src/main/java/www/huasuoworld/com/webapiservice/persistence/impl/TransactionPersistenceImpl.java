package www.huasuoworld.com.webapiservice.persistence.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.SQLRowStream;
import io.vertx.ext.web.api.OperationResponse;
import www.huasuoworld.com.webapiservice.jdbc.MysqlConnection;
import www.huasuoworld.com.webapiservice.models.Transaction;
import www.huasuoworld.com.webapiservice.persistence.TransactionPersistence;

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


  private MysqlConnection mysqlConnection;
  private Map<String, Transaction> transactions;

  public TransactionPersistenceImpl(MysqlConnection mysqlConnection) {
    this.mysqlConnection = mysqlConnection;
    this.transactions = new HashMap<>();
  }

  @Override
  public List<Transaction> getFilteredTransactions(Predicate<Transaction> p) {
    return transactions.values().stream().filter(p).collect(Collectors.toList());
  }

  @Override
  public void getTransaction(String transactionId, Handler<AsyncResult<OperationResponse>> resultHandler) {
    mysqlConnection.getClient().getConnection(conn -> {
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
            if (row == null) {
              resultHandler.handle(Future.succeededFuture(new OperationResponse().setStatusCode(404).setStatusMessage("Not Found")));
            } else {
              // do something with the row...
              System.out.println(row.encode());
              JsonObject transaction = row.getJsonObject(0);
              resultHandler.handle(Future.succeededFuture(OperationResponse.completedWithJson(transaction)));
            }
          }).endHandler(v -> {
            // no more data available, close the connection
            connection.close(done -> {
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
    mysqlConnection.getClient().getConnection(conn -> {
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

    mysqlConnection.getClient().getConnection(conn -> {
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
}
