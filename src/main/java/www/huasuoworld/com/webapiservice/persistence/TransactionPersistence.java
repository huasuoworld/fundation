package www.huasuoworld.com.webapiservice.persistence;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.web.api.OperationResponse;
import www.huasuoworld.com.webapiservice.models.Transaction;

import java.util.List;
import java.util.function.Predicate;


/**
 * This interface represents a persistence layer of your application
 *
 * @author slinkydeveloper
 */

public interface TransactionPersistence {


  /**
   * Factory method to instantiate TransactionPersistence
   *
   * @return
   */
//  static TransactionPersistence create() {
//    return new TransactionPersistenceImpl();
//  }

  List<Transaction> getFilteredTransactions(Predicate<Transaction> p);

  void getTransaction(String transactionId, Handler<AsyncResult<OperationResponse>> resultHandler);

  Transaction addTransaction(Transaction t);

  boolean removeTransaction(String transactionId);

  boolean updateTransaction(String transactionId, Transaction transaction);
}
