package com.uniksoft.broker.db.migration;

import com.uniksoft.broker.config.DbConfig;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class FlywayMigration {

  private static final Logger LOG = LoggerFactory.getLogger(FlywayMigration.class);

  public static Future<Void> migrate(Vertx vertx, DbConfig dbConfig) {
    return vertx.<Void>executeBlocking(promise -> {
      // Flyway migration is blocking because it uses JDBC
      execute(dbConfig);
      promise.complete();
    }).onFailure(err -> LOG.error("Failed to migrate db schema error: ", err));
  }

  private static void execute(DbConfig dbConfig) {
    final String jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s",
      dbConfig.getHost(),
      dbConfig.getPort(),
      dbConfig.getDatabase()
    );
    LOG.info("Migrating database using JDBC URL: {}", jdbcUrl);

    final Flyway flyway = Flyway.configure()
      .dataSource(jdbcUrl, dbConfig.getUser(), dbConfig.getPassword())
      .schemas("broker")
      .defaultSchema("broker")
      .load();

    var current = Optional.ofNullable(flyway.info().current());
    current.ifPresent(info -> LOG.info("Current database version: {}", info.getVersion()));

    var pendingMigrations = flyway.info().pending();
    LOG.info("Migrating database using Pending Migrations: {}", printMigrations(pendingMigrations));

    flyway.migrate();
  }

  private static String printMigrations(MigrationInfo[] pendingMigrations) {
    if (Objects.isNull(pendingMigrations)) {
      return "[]";
    }
    return Arrays.stream(pendingMigrations)
      .map(each -> each.getVersion() + " - " + each.getDescription())
      .collect(Collectors.joining(",","[","]"));
  }
}
