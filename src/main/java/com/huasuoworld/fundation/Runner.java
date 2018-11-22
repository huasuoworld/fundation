package com.huasuoworld.fundation;

import com.google.inject.servlet.GuiceFilter;
import com.huasuoworld.fundation.guice.InitializeGuiceModulesContextListener;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

import javax.xml.bind.JAXBContext;

public class Runner {

    private static Server server;

    public static void main(String[] args) throws Exception {
        new Runner().run();
    }

    private void run() throws Exception {
        createServer();
        bindGuiceContextToServer();
        startServer();
        waitForServerToFinnish();
    }

    private void bindGuiceContextToServer() {
        ServletContextHandler context = createRootContext();
        serveGuiceContext(context);
    }

    private void serveGuiceContext(ServletContextHandler context) {
        bindGuiceContextAndFilter(context);
        addDefaultServletToContext(context);
    }

    private void addDefaultServletToContext(ServletContextHandler context) {
        /*
         * Jetty requires some servlet to be bound to the path, otherwise request is just skipped. This prevents Guice
         * from handling the request, because it is done through filter.
         */
        context.addServlet(DefaultServlet.class, "/");
    }

    private void bindGuiceContextAndFilter(ServletContextHandler context) {
        context.addEventListener(new InitializeGuiceModulesContextListener());
        context.addFilter(GuiceFilter.class, "/*", EnumSet.allOf(DispatcherType.class));
    }

    private ServletContextHandler createRootContext() {
        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("");
        server.setHandler(context);
        return context;
    }

    private void waitForServerToFinnish() throws InterruptedException {
        server.join();
    }

    private void startServer() throws Exception {
        server.start();
    }

    private void createServer() {
        server = new Server(10010);
    }
}
