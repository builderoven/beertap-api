package com.beertap.beertap_api.config;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("prod")
public class DataSourceConfig {

    private static final Logger log = LoggerFactory.getLogger(DataSourceConfig.class);

    @Bean
    public DataSource dataSource() {
        String databaseUrl = System.getenv("DATABASE_URL");
        String pgHost = System.getenv("PGHOST");
        String pgPort = System.getenv("PGPORT");
        String pgDatabase = System.getenv("PGDATABASE");
        String pgUser = System.getenv("PGUSER");
        String pgPassword = System.getenv("PGPASSWORD");

        log.info("DATABASE_URL={}", databaseUrl);
        log.info("PGHOST={}, PGPORT={}, PGDATABASE={}, PGUSER={}", pgHost, pgPort, pgDatabase, pgUser);

        if (databaseUrl != null && !databaseUrl.startsWith("jdbc:")) {
            databaseUrl = "jdbc:" + databaseUrl;
        } else if (databaseUrl == null && pgHost != null) {
            databaseUrl = "jdbc:postgresql://" + pgHost + ":" + pgPort + "/" + pgDatabase;
        }

        if (databaseUrl == null) {
            throw new IllegalStateException(
                "DATABASE_URL not set. Add a PostgreSQL database to your Railway project and connect it to this service."
            );
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
