# Hogan

![Hogan Image](https://raw.githubusercontent.com/wiki/disc99/hogan/images/hogan.png)

[![MIT license](http://img.shields.io/badge/license-MIT-brightgreen.svg)](http://opensource.org/licenses/MIT)
[![Download](https://api.bintray.com/packages/disc99/maven/hogan/images/download.svg) ](https://bintray.com/disc99/maven/hogan/_latestVersion)
[![Build Status](https://travis-ci.org/disc99/hogan.svg?branch=master)](https://travis-ci.org/disc99/hogan)
[![Coverage Status](https://coveralls.io/repos/github/disc99/hogan/badge.svg?branch=master)](https://coveralls.io/github/disc99/hogan?branch=master)

Hogan is the utility library which allows you to access DB intuitively.


## Description
As you know, DB processing needs boilerplate code.<br>
Hogan expresses data structure with Spock like DSL (known as "data table DSL").<br>
That's why you can access DB intuitively.

## Features
- Insert multiple tables
- Assert multiple tables (@Beta)


## Usage
### Add dependency
At first, please add following dependency and repository.

```groovy
dependencies {
  testCompile "io.disc99:hogan:0.9.2"
}
repositories {
  jcenter()
}
```
### Enable Hogan DSL
There are two ways to enable Hogan DSL.<br>
One way is to create subclass of `Specification`.<br>
The other way is to add `@EnableHogan` annotation to the target class.

### Create `Database` instance
Create `Database` class which processes DB access.<br>
Constractor of `Database` is a deregater to create `groovy.sql.Sql`(new Sql or Sql.newInstance).<br>
If you need more information, check the following link.<br>
[`groovy.sql.Sql`](http://docs.groovy-lang.org/docs/latest/html/gapi/groovy/sql/Sql.html)

```groovy
Database db = new Database("jdbc:h2:mem:", "org.h2.Driver")
```

### Feature: insert
Describe table name whith label, and execute SQL each of it.<br>
Execute 'Insert' according to the table definition.<br>
And of cource you can deal with the number of labels.

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

Followings are acutual executed SQL.

```sql
INSERT INTO ITEM_MASTER (ID, NAME, PRICE) VALUES (1, 'Apple', 500)
INSERT INTO ITEM_MASTER (ID, NAME, PRICE) VALUES (2, 'Orange', 250)
INSERT INTO SALES (ID, DAY, ITEM_ID, NUM) VALUES (1, '2015-04-01', 1, 3)
INSERT INTO SALES (ID, DAY, ITEM_ID, NUM) VALUES (2, '2015-04-02', 2, 1)
INSERT INTO SALES (ID, DAY, ITEM_ID, NUM) VALUES (3, '2015-04-02', 1, 2)
```

### Feature: assert (@Beta)
Assert to the table according to the definition.<br>
And undefined columns will be ignored.

```groovy
class HoganSpec extends Specification {
  def test() {
    when:
    // ...

    then:
    db.assert {
      item_master:
      id  | name
      100 | 'Banana'
      101 | 'Pine'
    }
  }
}
```

You will get following message when there's any discard.

```
assert actual == expected
       |      |  |
       |      |  [[ID:100, NAME:Banana], [ID:101, NAME:Pine]]
       |      false
       [[ID:100, NAME:Banana], [ID:101, NAME:Pineapple]]
```

## License
[MIT License](https://github.com/disc99/hogan/blob/master/LICENSE)
