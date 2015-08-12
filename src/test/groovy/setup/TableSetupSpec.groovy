package setup

import groovy.sql.Sql
import spock.lang.Specification

import static setup.TableSetup.insert

class TableSetupSpec extends Specification {

    def "insert data table"() {

        Sql h2 = Sql.newInstance("jdbc:h2:mem:", "org.h2.Driver")
        h2.execute '''
            |create table item_master (
            | id int,
            | name varchar(10),
            | price int);
            |create table sales (
            | id int,
            | date varchar(10),
            | item_id int,
            | count int);
            '''.stripMargin().toString()

        when:
        insert h2, {
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
                1  | '2015-04-02' | 2       | 1
                1  | '2015-04-02' | 1       | 2
            }
        }


        then:
        h2.rows("select * from sales s join item_master i on i.id = s.item_id").toString() == '[[ID:1, DATE:2015-04-01, ITEM_ID:1, COUNT:3, NAME:Apple, PRICE:500], [ID:1, DATE:2015-04-02, ITEM_ID:1, COUNT:2, NAME:Apple, PRICE:500], [ID:2, DATE:2015-04-02, ITEM_ID:2, COUNT:1, NAME:Orange, PRICE:250]]'
    }
}
