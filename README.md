# Hogan

[![MIT license](http://img.shields.io/badge/license-MIT-brightgreen.svg)](http://opensource.org/licenses/MIT)
[![Download](https://api.bintray.com/packages/disc99/maven/hogan/images/download.svg) ](https://bintray.com/disc99/maven/hogan/_latestVersion)
[![Build Status](https://travis-ci.org/disc99/hogan.svg?branch=master)](https://travis-ci.org/disc99/hogan)
[![Coverage Status](https://coveralls.io/repos/github/disc99/hogan/badge.svg?branch=master)](https://coveralls.io/github/disc99/hogan?branch=master)

Hoganはデータベースへのアクセス処理を直感的に行うためのユーティリティライブラリです。

## Description
データベースへのアクセス処理は、しばし冗長で、分かりにくいことがあります。
Hoganは、Spockのようなデータテーブル構造のDSLを用いてデータを表現し、各処理を実行します。


## Features
- Insert multi tables
- Assert multi tables


## Usage
### Add dependency
Hoganを使用する場合には以下のDependencyとリポジトリを追加してください。

```groovy
dependencies {
  testCompile "disc99:hogan:0.9.1"
}
repositories {
  jcenter()
}
```

### Create `Database` instance
データベースへのアクセス処理を行う`Database`クラスの生成を行います。
`Database`コンストラクタは`groovy.sql.Sql`生成のための、コンストラクタ、またはnewInstanceメソッドへの委譲処理です。
詳細を知りたい場合には、[`groovy.sql.Sql`](http://docs.groovy-lang.org/docs/latest/html/gapi/groovy/sql/Sql.html)から詳細を確認することが出来ます。

```groovy
Database db = new Database("jdbc:h2:mem:", "org.h2.Driver")
```

### Enable Hogan DSL
Hoganは、そのDSLをクラス内で有効にする必要があります。

`Specification`のサブクラスの場合は自動的に有効になります。
また、それ以外のクラスで使用する場合には、`@EnableHogan`をクラスに付加することでも有効にできます。

### Feature: insert
記述したテーブル定義に従ってInsert処理を実行します。
ラベルがテーブル名となり、記述したラベル単位でSQLの実行します。
また、複数のラベルを記述することで、それぞれのテーブルに対するinsertも可能です。

```groovy
class HoganSpec extends Specification {
  def test() {
    setup:
    db.insert {
      item_master:
      id | name     | price
      1  | 'Apple'  | 500
      2  | 'Orange' | 250

      sales:
      id | day          | item_id | num
      1  | '2015-04-01' | 1       | 3
      2  | '2015-04-02' | 2       | 1
      3  | '2015-04-02' | 1       | 2
    }

    expect:
    // ...
  }
}
```

実際には以下のようなSQLが実行されます。

```sql
INSERT INTO ITEM_MASTER (ID, NAME, PRICE) VALUES (1, 'Apple', 500)
INSERT INTO ITEM_MASTER (ID, NAME, PRICE) VALUES (2, 'Orange', 250)
INSERT INTO SALES (ID, DAY, ITEM_ID, NUM) VALUES (1, '2015-04-01', 1, 3)
INSERT INTO SALES (ID, DAY, ITEM_ID, NUM) VALUES (2, '2015-04-02', 2, 1)
INSERT INTO SALES (ID, DAY, ITEM_ID, NUM) VALUES (3, '2015-04-02', 1, 2)
```

### Feature: expect (@Beta)
記述した定義に従い、テーブルのアサートを行います。
また、記述されていないカラムはアサートの対象から除外されます。

```groovy
class HoganSpec extends Specification {
  def test() {
    when:
    // ...

    then:
    db.expect {
      item_master:
      id  | name
      100 | 'Banana'
      101 | 'Pine'
    }
  }
}
```

仮にテーブルのデータに誤りがある場合には、以下のような結果が出力されます。

```
assert actual == expected
       |      |  |
       |      |  [[ID:100, NAME:Banana], [ID:101, NAME:Pineapple]]
       |      false
       [[ID:100, NAME:Banana], [ID:101, NAME:Pine]]
```

## Author

[@disc99](https://github.com/disc99)

## License
[MIT License](https://github.com/disc99/hogan/blob/master/LICENSE)
