# Hogan
Hogan is RDBMS table disc99.hogan.setup DSL.


## Latest release

To add a dependency:

```groovy:
dependencies {
  testCompile "disc99:hogan:2.0.0"
}
repositories {
  jcenter()
}
```


## How to use

If dependent on Spock.
Enabled by Global AST in `Specification` class.

Dependency Hogan only.
`@EnableHogan` annotate target class.

```groovy
class HoganSpec extends Specification {

  TableSetup table = new TableSetup("jdbc:h2:mem:", "org.h2.Driver")

  def test() {
    setup:
    table.insert {
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

## License
[MIT License](https://github.com/disc99/hogan/blob/master/License)
