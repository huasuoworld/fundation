package com.huasuoworld.fundation.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.huasuoworld.fundation.service.IncrementalNextValueSource;
import com.huasuoworld.fundation.service.NextValueSource;

public class BindServicesModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(NextValueSource.class).to(IncrementalNextValueSource.class).in(Singleton.class);
    }

}
