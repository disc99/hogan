package disc99.hogan.setup

import groovy.sql.Sql
import spock.lang.Specification

import java.sql.Timestamp
import java.sql.Date

@EnableHogan
class TableSetupSpec extends Specification {

    Sql sql
    TableSetup table

    def setup() {
        sql = Sql.newInstance("jdbc:h2:mem:", "org.h2.Driver")
        table = new TableSetup(sql)

        sql.execute '''
            |CREATE TABLE item_master (
            |  ID INT,
            |  NAME VARCHAR,
            |  PRICE INT);
            |CREATE TABLE sales (
            |  ID INT,
            |  DAY VARCHAR,
            |  ITEM_ID INT,
            |  COUNT INT);
            |CREATE TABLE persons (
            |  ID INT,
            |  AGE NUMBER,
            |  NAME VARCHAR,
            |  DAY DATE,
            |  START TIMESTAMP);
            '''.stripMargin().toString()
    }

    def "insert data"() {
        when:
        table.insert {
            item_master:
            id | name     | price
            1  | 'Apple'  | 500
            2  | 'Orange' | 250

            sales:
            id | day          | item_id | count
            1  | '2015-04-01' | 1       | 3
            1  | '2015-04-02' | 2       | 1
            1  | '2015-04-02' | 1       | 2
        }

        then:
        sql.rows("select * from sales").toString() == '[[ID:1, DAY:2015-04-01, ITEM_ID:1, COUNT:3], [ID:1, DAY:2015-04-02, ITEM_ID:2, COUNT:1], [ID:1, DAY:2015-04-02, ITEM_ID:1, COUNT:2]]'
        sql.rows("select * from item_master").toString() == '[[ID:1, NAME:Apple, PRICE:500], [ID:2, NAME:Orange, PRICE:250]]'
    }

    def "multi format"() {
        when:
        table.insert {
            persons:
            id | age | name  | day         | start
            1  | 2   | 'tom' | new Date(1) | Timestamp.valueOf("1970-01-01 00:00:00.001")
        }

        then:
        sql.rows("select * from persons").toString() == '[[ID:1, AGE:2, NAME:tom, DAY:1970-01-01, START:1970-01-01 00:00:00.001]]'
    }
}
