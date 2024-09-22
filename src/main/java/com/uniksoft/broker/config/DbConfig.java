package com.uniksoft.broker.config;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DbConfig {
//  String host = "localhost";
//  int port = 5432;
//  String database = "vertx-share-broker";
//  String user = "postgres";
//  String password = "secret";

  String host = "localhost";
  int port = 3307;
  String database = "vertx-share-broker";
  String user = "root";
  String password = "secret";

  @Override
  public String toString() {
    return "DbConfig{" +
      "user='" + user + '\'' +
      ", database='" + database + '\'' +
      ", port=" + port +
      ", host='" + host + '\'' +
      '}';
  }
}
