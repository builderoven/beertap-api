package com.beertap.beertap_api.config;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    private static final Logger log = LoggerFactory.getLogger(DataSourceConfig.class);

    @Bean
    @ConditionalOnExpression("'${DATABASE_URL:}' != ''")
    public DataSource postgresDataSource() {
        String databaseUrl = System.getenv("DATABASE_URL");
        String pgUser = System.getenv("PGUSER");
        String pgPassword = System.getenv("PGPASSWORD");

        log.info("Configuring PostgreSQL DataSource from DATABASE_URL");

        if (!databaseUrl.startsWith("jdbc:")) {
            databaseUrl = "jdbc:" + databaseUrl;
        }

        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(databaseUrl);
        ds.setDriverClassName("org.postgresql.Driver");
        if (pgUser != null) ds.setUsername(pgUser);
        if (pgPassword != null) ds.setPassword(pgPassword);
        ds.setMaximumPoolSize(10);
        return ds;
    }
}
