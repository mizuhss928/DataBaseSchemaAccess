package com.mizuhss928.dbconnection;

import java.util.Arrays;
import java.util.Objects;

public enum DataType {
  integer("integer"),
  varchar("character varying"),
  numeric("numeric"),
  bool("boolean"),
  jsonb("jsonb");

  private final String value;

  DataType(String value) {
    this.value = value;
  }

  public static DataType getByValue(String value) {
    return Arrays.stream(DataType.values())
        .filter(dataType -> Objects.equals(dataType.value, value))
        .findFirst()
        .orElseThrow();
  }
}
