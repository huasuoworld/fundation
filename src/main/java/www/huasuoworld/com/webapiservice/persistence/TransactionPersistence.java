package www.huasuoworld.com.webapiservice.persistence;

import www.huasuoworld.com.webapiservice.models.Transaction;
import www.huasuoworld.com.webapiservice.persistence.impl.TransactionPersistenceImpl;

import java.util.List;
import java.util.Optional;
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
  static TransactionPersistence create() {
    return new TransactionPersistenceImpl();
  }

  List<Transaction> getFilteredTransactions(Predicate<Transaction> p);

  Optional<Transaction> getTransaction(String transactionId);

  Transaction addTransaction(Transaction t);

  boolean removeTransaction(String transactionId);

  boolean updateTransaction(String transactionId, Transaction transaction);
}
