package www.huasuoworld.com.webapiservice.services;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.web.api.OperationRequest;
import io.vertx.ext.web.api.OperationResponse;
import io.vertx.ext.web.api.generator.WebApiServiceGen;
import www.huasuoworld.com.webapiservice.models.Transaction;
import www.huasuoworld.com.webapiservice.persistence.TransactionPersistence;
import www.huasuoworld.com.webapiservice.services.impl.TransactionsManagerServiceImpl;


import java.util.List;


/**
 * This interface describes the Transactions Manager Service. Note that all methods has same name of corresponding operation id
 */

@WebApiServiceGen

public interface TransactionsManagerService {


  static TransactionsManagerService create(TransactionPersistence persistence) {
    return new TransactionsManagerServiceImpl(persistence);
  }


  void getTransactionsList(
    List<String> from,
    List<String> to,
    List<String> message,
    OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler);

  void createTransaction(
    Transaction body,
    OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler);

  void getTransaction(
    String transactionId,
    OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler);

  void updateTransaction(
    String transactionId,
    Transaction body,
    OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler);


  void deleteTransaction(
    String transactionId,
    OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler);

}
