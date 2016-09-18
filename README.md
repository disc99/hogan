# hogan
hogan is RDBMS table setup DSL.


## Latest release
To add a dependency on hogan using Maven, use the following:

```xml
<dependency>
  <groupId>disc99</groupId>
  <artifactId>hogan</artifactId>
  <version>2.0.0</version>
</dependency>
```

To add a dependency using Gradle:

```groovy:
dependencies {
  testCompile "disc99:hogan:2.0.0"
}
```


## How to use


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
        1  | '2015-04-01' | 1       | 3
        2  | '2015-04-02' | 2       | 1
        3  | '2015-04-02' | 1       | 2
    }
    // -> insert into item_master (id, name, price) values (1, 'Apple', 500)
    //    insert into item_master (id, name, price) values (2, 'Orange', 250)
    //    insert into sales (id, date, item_id, count) values (1, '2015-04-01', 1, 3)
    //    insert into sales (id, date, item_id, count) values (1, '2015-04-02', 2, 1)
    //    insert into sales (id, date, item_id, count) values (1, '2015-04-02', 1, 2)

    expect:
    // ...
  }
```
