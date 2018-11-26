package com.huasuoworld.fundation.guice;

import org.mybatis.guice.XMLMyBatisModule;

import java.util.Properties;

public class MyXMLMyBatisModule extends XMLMyBatisModule {
    @Override
    protected void initialize() {
        setEnvironmentId("test");
        setClassPathResource("mapper/mybatis-config.xml");
        addProperties(getConnectionProperties("test"));
    }

    private final static Properties getConnectionProperties(String schema) {
        final Properties myBatisProperties = new Properties();

        myBatisProperties.setProperty("mybatis.environment.id", "test");
        myBatisProperties.setProperty("JDBC.driver", "com.mysql.jdbc.Driver");
        myBatisProperties.setProperty("JDBC.url", "jdbc:mysql://localhost:3306/" + schema);
        myBatisProperties.setProperty("JDBC.username", "root");
        myBatisProperties.setProperty("JDBC.password", "root");
        myBatisProperties.setProperty("JDBC.autoCommit", "true");

        return myBatisProperties;
    }
}
