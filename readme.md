# DataBaseSchemaAccess
information_schemaへアクセスする際の挙動を動作確認するためのプログラム

## Getting Started

・プロジェクト直下にあるdocker-composeを利用してpostgresqlのデータベースサーバを立ち上げる
```terminal
docker-compose up -d
```

・pom.xmlに記載されている依存関係を解決する
```terminal
mvn install
```

・com.mizuhss928.dbconnection.Mainクラスを実行する

## Prerequisites
・Java 17
・Docker


## Memo
information_schemaはschemaを跨ぐ全ての情報を格納しているため、
クエリの際には必ずwhere句にtable_schemaを指定する。