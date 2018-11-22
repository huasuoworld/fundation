package com.huasuoworld.fundation.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.huasuoworld.fundation.guice.BindJerseyResourcesModule;
import com.huasuoworld.fundation.guice.BindServicesModule;

public class InitializeGuiceModulesContextListener extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new BindServicesModule(), new BindJerseyResourcesModule());
    }
}
