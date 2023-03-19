package com.mizuhss928.dbconnection;

public class ColumnInfo {
  private final String columnName;
  private final DataType dataType;

  public ColumnInfo(String columnName, DataType dataType) {
    this.columnName = columnName;
    this.dataType = dataType;
  }

  public String getColumnName() {
    return columnName;
  }

  public DataType getDataType() {
    return dataType;
  }
}
