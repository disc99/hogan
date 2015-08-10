# table-setup
table-setup is RDBMS table setup DSL.

## How to use

```groovy
import static setup.TableSetup.*

Sql sql = Sql.newInstance("jdbc:h2:mem:", "org.h2.Driver")

insert sql, {
    table 'item_master'
    rows {
        id | name     | price
        1  | 'Apple'  | 500
        2  | 'Orange' | 250
    }
}, {
    table 'sales'
    rows {
        id | date         | item_id | count
        1  | '2015-04-01' | 1       | 3
        2  | '2015-04-02' | 2       | 1
        3  | '2015-04-02' | 1       | 2
    }
}
// -> 
//  insert into item_master (id, name, price) values (1, Apple, 500)
//  insert into item_master (id, name, price) values (2, Orange, 250)
//  insert into sales (id, date, item_id, count) values (1, 2015-04-01, 1, 3)
//  insert into sales (id, date, item_id, count) values (1, 2015-04-02, 2, 1)
//  insert into sales (id, date, item_id, count) values (1, 2015-04-02, 1, 2)

```
