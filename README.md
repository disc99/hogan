# Hogan

Hogan is RDBMS utility DSL.

[![MIT license](http://img.shields.io/badge/license-MIT-brightgreen.svg)](http://opensource.org/licenses/MIT)
[![Download](https://api.bintray.com/packages/disc99/maven/hogan/images/download.svg) ](https://bintray.com/disc99/maven/hogan/_latestVersion)
[![Build Status](https://travis-ci.org/disc99/hogan.svg?branch=master)](https://travis-ci.org/disc99/hogan)
[![Coverage Status](https://coveralls.io/repos/github/disc99/hogan/badge.svg?branch=master)](https://coveralls.io/github/disc99/hogan?branch=master)


## Latest release

To add a dependency:

```groovy
dependencies {
  testCompile "disc99:hogan:0.9.1"
}
repositories {
  jcenter()
}
```


## Quick start

### Feature: `insert`

If dependent on Spock.
Enabled by Global AST in `Specification` class.

```groovy
class HoganSpec extends Specification {

  Database db = new Database("jdbc:h2:mem:", "org.h2.Driver")

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

Execute SQL

```sql
INSERT INTO ITEM_MASTER (ID, NAME, PRICE) VALUES (1, 'Apple', 500)
INSERT INTO ITEM_MASTER (ID, NAME, PRICE) VALUES (2, 'Orange', 250)
INSERT INTO SALES (ID, DAY, ITEM_ID, NUM) VALUES (1, '2015-04-01', 1, 3)
INSERT INTO SALES (ID, DAY, ITEM_ID, NUM) VALUES (1, '2015-04-02', 2, 1)
INSERT INTO SALES (ID, DAY, ITEM_ID, NUM) VALUES (1, '2015-04-02', 1, 2)
```

### Feature: `expect`

```groovy
class HoganSpec extends Specification {

  Database db = new Database("jdbc:h2:mem:", "org.h2.Driver")

  def test() {
    when:
    // ...

    then:
    db.expect {
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
  }
}
```

## FAQ
1. Can be calling other than `Specification` class?

> `@EnableHogan` annotate target class.

## License
[MIT License](https://github.com/disc99/hogan/blob/master/LICENSE)
