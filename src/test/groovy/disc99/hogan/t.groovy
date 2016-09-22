//package disc99.hogan
//
//import disc99.hogan.setup.EnableHogan
//import disc99.hogan.setup.TableSetup
//import groovy.sql.Sql
//import spock.lang.Specification
//
//
//@EnableHogan
//class T {
//
//    Sql sql
//    TableSetup table
//
//    def setup() {
//        sql = Sql.newInstance("jdbc:h2:mem:", "org.h2.Driver")
//        table = new TableSetup(sql)
//
//        sql.execute '''
//            |CREATE TABLE item_master (
//            |  ID INT,
//            |  NAME VARCHAR(10),
//            |  PRICE INT);
//            |CREATE TABLE sales (
//            |  ID INT,
//            |  DATE VARCHAR(10),
//            |  ITEM_ID INT,
//            |  COUNT INT);
//            '''.stripMargin().toString()
//    }
//
//    def xxx() {
//
//        setup()
//
//        table.insert {
//            item_master:
//            id | name     | price
//            1  | 'Apple'  | 500
//            2  | 'Orange' | 250
//
//            sales:
//            id | date         | item_id | count
//            1  | '2015-04-01' | 1       | 3
//            1  | '2015-04-02' | 2       | 1
//            1  | '2015-04-02' | 1       | 2
//        }
//
//
//        assert sql.rows("select * from sales").toString() == '[[ID:1, DATE:2015-04-01, ITEM_ID:1, COUNT:3], [ID:1, DATE:2015-04-02, ITEM_ID:2, COUNT:1], [ID:1, DATE:2015-04-02, ITEM_ID:1, COUNT:2]]'
//        assert sql.rows("select * from item_master").toString() == '[[ID:1, NAME:Apple, PRICE:500], [ID:2, NAME:Orange, PRICE:250]]'
//    }
//}
//
//new T().xxx()
