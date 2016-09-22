package disc99.hogan.setup

import groovy.sql.Sql
import disc99.hogan.parser.TableParser

class TableSetup {

    Sql sql

    TableSetup(Sql sql) {
        this.sql = sql
    }

    TableSetup(String url, String driverClassName) {
        this.sql = Sql.newInstance(url, driverClassName)
    }

    def insert(tables) {
        tables.each {name, table ->
            TableParser.asTable(table).toMapList().each {
                sql.dataSet(name).add(it)
            }
        }
    }
}


