package com.huasuoworld.fundation;

import com.google.inject.servlet.GuiceFilter;
import com.huasuoworld.fundation.guice.InitializeGuiceModulesContextListener;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

public class Runner {

    private static Server server;

    public static void main(String[] args) {

    }

    private void startServer() throws Exception {
        server.start();
    }

    private void createServer() {
        server = new Server(10005);
    }

    private void waitForServerToFinnish() throws InterruptedException {
        server.join();
    }

    private ServletContextHandler createRootContext() {
        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("");
        server.setHandler(context);
        return context;
    }

    private void bindGuiceContextAndFilter(ServletContextHandler context) {
        context.addEventListener(new InitializeGuiceModulesContextListener());
        context.addFilter(GuiceFilter.class, "/*", EnumSet.allOf(DispatcherType.class));
    }
}
