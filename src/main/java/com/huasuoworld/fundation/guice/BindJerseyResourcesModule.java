package com.huasuoworld.fundation.guice;

import com.google.inject.servlet.ServletModule;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import java.util.Set;

public class BindJerseyResourcesModule extends ServletModule {

    @Override
    protected void configureServlets() {
        bindResources();
        serveBoundResources();
    }

    private void bindResources() {
        for (Class<?> resource : lookupResources()) {
            bind(resource);
        }
    }

    private Set<Class<?>> lookupResources() {
        PackagesResourceConfig resourceConfig = new PackagesResourceConfig("com.github.reap.rest.resource");
        return resourceConfig.getClasses();
    }

    private void serveBoundResources() {
        serve("/*").with(GuiceContainer.class);
    }
}