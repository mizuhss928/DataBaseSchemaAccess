-- テーブル作成（publicスキーマ）
create table public.sample (
  id serial primary key,
  col1 varchar(10),
  col2 numeric(8, 2),
  col3 boolean
);

-- レコード作成（publicスキーマ）
insert into public.sample (col1, col2, col3) values ('レコード1のカラム', 2500.05, true);
insert into public.sample (col1, col2, col3) values ('レコード2のカラム', 5630.10, false);
insert into public.sample (col1, col2, col3) values ('レコード3のカラム', 9800.23, true);

-- テーブル作成（appスキーマ）
create table app.sample (
  id serial primary key,
  col1 varchar(10),
  col2 numeric(8, 2),
  col3 jsonb
);

-- レコード作成（appスキーマ）
insert into app.sample (col1, col2, col3) values ('レコード1のカラム', 2434, '{"a": 10, "b": 20}');