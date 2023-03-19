package com.mizuhss928.dbconnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class SchemaAccess {
  private static final String ACCESS_TABLE = "sample";
  private final Connection connection;
  private final String schema;

  public SchemaAccess(Connection connection, String schema) throws SQLException {
    connection.setSchema(schema);
    this.connection = connection;
    this.schema = schema;
  }

  public void execute() throws SQLException {
    // information_schemaからカラム情報を取得
    var columnInfoList = this.getColumnInfo();

    // 一番長いカラム名のlengthを取得
    var maxColumnNameLength = columnInfoList.stream()
        .max(Comparator.comparing(o -> o.getColumnName().length()))
        .map(columnInfo -> columnInfo.getColumnName().length())
        .orElseThrow();

    // テーブル名は定数なのでSQLインジェクションは考慮しない。もし動的に変えるなら対応必須
    var ps = connection.prepareStatement("select * from " + ACCESS_TABLE);
    var resultSet = ps.executeQuery();

    var count = 0;
    while (resultSet.next()) {
      for (ColumnInfo columnInfo : columnInfoList) {
        // 一番長いカラム名に合わせてスペースパディング
        var columnName = String.format("%" + maxColumnNameLength + "s", columnInfo.getColumnName());

        // カラムのデータ型に合わせてresultSetから値を取得
        var value = this.getValue(columnInfo, resultSet);

        // カラム名と値をセットで出力
        System.out.println(columnName + ": " + value);
      }
      System.out.println("-----");
      count++;
    }
    System.out.println(this.schema + "スキーマで" + count + "件のレコードを取得しました。");
    System.out.println("=======================");
  }

  private List<ColumnInfo> getColumnInfo() throws SQLException {
    var ps = this.connection.prepareStatement("select * from information_schema.columns where table_schema = ? and table_name = ? order by ordinal_position;");
    ps.setString(1, this.schema);
    ps.setString(2, ACCESS_TABLE);
    var resultSet = ps.executeQuery();

    var columnInfoList = new ArrayList<ColumnInfo>();
    while (resultSet.next()) {
      var columnName = resultSet.getString("column_name");
      var dataType = resultSet.getString("data_type");
      var columnInfo = new ColumnInfo(columnName, DataType.getByValue(dataType));
      columnInfoList.add(columnInfo);
    }
    return columnInfoList;
  }

  private Object getValue(ColumnInfo columnInfo, ResultSet resultSet) throws SQLException {
    var columnName = columnInfo.getColumnName();
    return switch (columnInfo.getDataType()) {
      case varchar -> resultSet.getString(columnName);
      case integer -> resultSet.getInt(columnName);
      case numeric -> resultSet.getBigDecimal(columnName);
      case bool -> resultSet.getBoolean(columnName);
      case jsonb -> resultSet.getObject(columnName, new HashMap<>());
    };
  }

}
