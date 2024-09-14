package com.uniksoft.broker.config;

import java.util.Arrays;
import java.util.List;

public class ConfigLoader {

  // Exposed Environment Variables
  public static final String DB_HOST = "DB_HOST";
  public static final String DB_PORT = "DB_PORT";
  public static final String DB_DATABASE = "DB_DATABASE";
  public static final String DB_USER = "DB_USER";
  public static final String DB_PASSWORD = "DB_PASSWORD";

  static final List<String> EXPOSED_ENVIRONMENT_VARIABLES = Arrays.asList(
    DB_HOST, DB_PORT, DB_DATABASE, DB_USER, DB_PASSWORD
  );
}
