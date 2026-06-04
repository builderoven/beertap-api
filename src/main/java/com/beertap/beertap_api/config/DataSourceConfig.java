package com.beertap.beertap_api.config;

import java.net.URI;
import java.net.URISyntaxException;

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
    public DataSource postgresDataSource() throws URISyntaxException {
        String databaseUrl = System.getenv("DATABASE_URL");

        log.info("Configuring PostgreSQL DataSource from DATABASE_URL");

        URI uri = new URI(databaseUrl);
        String host = uri.getHost();
        int port = uri.getPort();
        String dbname = uri.getPath().replaceFirst("^/", "");
        String user = uri.getUserInfo() != null ? uri.getUserInfo().split(":")[0] : null;
        String password = uri.getUserInfo() != null && uri.getUserInfo().contains(":")
                ? uri.getUserInfo().split(":")[1] : null;

        String pgUser = System.getenv("PGUSER");
        String pgPassword = System.getenv("PGPASSWORD");

        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:postgresql://" + host + ":" + port + "/" + dbname);
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUsername(pgUser != null ? pgUser : user);
        ds.setPassword(pgPassword != null ? pgPassword : password);
        ds.setMaximumPoolSize(10);
        return ds;
    }
}
