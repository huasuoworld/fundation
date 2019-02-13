package www.huasuoworld.com.webapiservice.persistence.impl;

import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.SQLConnection;
import www.huasuoworld.com.webapiservice.jdbc.MysqlConnection;
import www.huasuoworld.com.webapiservice.models.Transaction;
import www.huasuoworld.com.webapiservice.persistence.TransactionPersistence;

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
  public Optional<Transaction> getTransaction(String transactionId) {
    Map<String, Transaction> transactions = new HashMap<>();

    mysqlConnection.getConn(null).getConnection(conn -> {
      if (conn.failed()) {
        System.err.println(conn.cause().getMessage());
        return;
      }
      final SQLConnection connection = conn.result();

      connection.queryWithParams("select * from transaction where id = ?", new JsonArray().add(transactionId), rs -> {
        Transaction transaction = null;
        if (rs.failed()) {
          System.err.println("Cannot retrieve the data from the database");
          rs.cause().printStackTrace();
          return;
        }

        for (JsonArray line : rs.result().getResults()) {
          transaction = new Transaction(line.getJsonObject(0));
          transactions.put(transactionId, transaction);
          System.out.println(line.encode());
        }

        // and close the connection
        connection.close(done -> {
          if (done.failed()) {
            throw new RuntimeException(done.cause());
          }
        });
      });
    });
    return Optional.ofNullable(transactions.get(transactionId));
  }

  @Override
  public Transaction addTransaction(Transaction t) {
    transactions.put(t.getId(), t);
    mysqlConnection.getConn(null).getConnection(conn -> {
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

    mysqlConnection.getConn(null).getConnection(conn -> {
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
