package www.huasuoworld.com.webapiservice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.api.contract.openapi3.OpenAPI3RouterFactory;
import io.vertx.serviceproxy.ServiceBinder;
import www.huasuoworld.com.webapiservice.guice.GuiceBinder;
import www.huasuoworld.com.webapiservice.services.TransactionsManagerService;


public class WebApiServiceExampleMainVerticle extends AbstractVerticle {

  HttpServer server;
  ServiceBinder serviceBinder;
//  MessageConsumer<JsonObject> consumer;
  Injector injector;
  /**
   * Start transaction service
   */
  private void startTransactionService() {
    serviceBinder = new ServiceBinder(vertx);
    TransactionsManagerService transactionsManagerService = injector.getInstance(TransactionsManagerService.class);
//    consumer =
    serviceBinder
      .setAddress("transactions_manager.myapp")
      .register(TransactionsManagerService.class, transactionsManagerService);
  }
  /**
   * This method constructs the router factory, mounts services and handlers and starts the http server with built router
   *
   * @return
   */
  private Future<Void> startHttpServer() {
    Future<Void> future = Future.future();
    OpenAPI3RouterFactory.create(this.vertx, "http://www.huasuoworld.com/head/openapi.json", openAPI3RouterFactoryAsyncResult -> {
      if (openAPI3RouterFactoryAsyncResult.succeeded()) {
        OpenAPI3RouterFactory routerFactory = openAPI3RouterFactoryAsyncResult.result();
        // Mount services on event bus based on extensions
        routerFactory.mountServicesFromExtensions();
        // Generate the router
        Router router = routerFactory.getRouter();

        server = vertx.createHttpServer(new HttpServerOptions().setPort(8080).setHost("localhost").setMaxHeaderSize(30480));
        server.requestHandler(router).listen(ar -> {
          System.out.println("server.requestHandler...");
          // Error starting the HttpServer

          if (ar.succeeded()) {
            System.out.println("server.requestHandler...ar.succeeded...");
            future.complete();
          } else {
            System.out.println("server.requestHandler...ar.cause...");
            future.fail(ar.cause());
          }
        });
      } else {
        System.out.println("future.fail....");
        // Something went wrong during router factory initialization
        future.fail(openAPI3RouterFactoryAsyncResult.cause());
      }
    });
    return future;
  }

  @Override
  public void start(Future<Void> future) {
    injector = Guice.createInjector(new GuiceBinder(vertx));
    startTransactionService();
    startHttpServer().setHandler(future.completer());
  }

  /**
   * This method closes the http server and unregister all services loaded to Event Bus
   */
  @Override
  public void stop() {
    this.server.close();
//    consumer.unregister();
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new WebApiServiceExampleMainVerticle());
  }
}
