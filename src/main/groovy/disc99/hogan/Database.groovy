package disc99.hogan

import disc99.hogan.parser.Column
import disc99.hogan.parser.Table
import groovy.sql.Sql
import disc99.hogan.parser.TableParser

class Database {

    Sql sql

    Database(Sql sql) {
        List
        this.sql = sql
    }

    Database(String url, String driverClassName) {
        this.sql = Sql.newInstance(url, driverClassName)
    }

    void insert(tables) {
        tables.each { name, table ->
            TableParser.asTable(table).toMapList().each {
                sql.dataSet(name).add(it)
            }
        }
    }

    void expect(tables) {
        println tables
        tables.each { name, table ->

            Table t = TableParser.asTable(table)
            Table upperColumnTable = new Table(t.columns.collect({ new Column(name: it.name.toUpperCase()) }), t.rows)

            List<Map> expected = sql.dataSet(name).rows().collect {
                it.collectEntries({ key, val -> [key.toUpperCase(), val] })
                        .subMap(upperColumnTable.columns.collect({ it.name }))
            }

            def actual = upperColumnTable.toMapList()
            assert actual == expected
        }
    }
}


