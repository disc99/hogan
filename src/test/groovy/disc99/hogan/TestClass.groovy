package disc99.hogan

import groovy.sql.Sql

import java.sql.Date
import java.sql.Timestamp

@EnableHogan
class TestClass {

    Sql sql
    Database db

    def setup() {
        sql = Sql.newInstance("jdbc:h2:mem:", "org.h2.Driver")
        db = new Database(sql)
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

    def go() {
        db.insert {
            persons:
            id | age | name  | day         | start
            1  | 2   | 'tom' | new Date(1) | Timestamp.valueOf("1970-01-01 00:00:00.001")
        }

        assert sql.rows("select * from persons").toString() == '[[ID:1, AGE:2, NAME:tom, DAY:1970-01-01, START:1970-01-01 00:00:00.001]]'
    }
}
