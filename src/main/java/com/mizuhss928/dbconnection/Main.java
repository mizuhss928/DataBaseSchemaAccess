package com.mizuhss928.dbconnection;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {
  // ドライバークラス名
  public static final String POSTGRES_DRIVER = "org.postgresql.Driver";

  // JDBC接続先情報
  public static final String JDBC_CONNECTION = "jdbc:postgresql://localhost:5432/sample_schema_db";

  // DB接続時のユーザ名とパスワード
  public static final String USERNAME = "postgres";
  public static final String PASSWORD = "postgres";

  // 対象外のスキーマ
  private static final List<String> NON_TARGET_SCHEMAS = List.of("information_schema", "pg_catalog", "pg_toast");

  public static void main(String[] args) {
    // Postgresのドライバークラスをロード
    try {
      Class.forName(POSTGRES_DRIVER);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }

    // DBからスキーマ一覧を取得
    var targetSchemas = new ArrayList<String>();
    try (var connection = DriverManager.getConnection(JDBC_CONNECTION, USERNAME, PASSWORD)) {
      var ps = connection.prepareStatement("select * from information_schema.schemata;");
      var rs = ps.executeQuery();

      while (rs.next()) {
        var schemaName = rs.getString("schema_name");
        if (!NON_TARGET_SCHEMAS.contains(schemaName)) {
          targetSchemas.add(schemaName);
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    // スキーマの件数分データベースアクセス
    targetSchemas.forEach(schema -> {
      try (var connectionSchema = DriverManager.getConnection(JDBC_CONNECTION, USERNAME, PASSWORD)) {
        var schemaAccess = new SchemaAccess(connectionSchema, schema);
        schemaAccess.execute();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    });
  }
}