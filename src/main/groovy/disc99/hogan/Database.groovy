package disc99.hogan

import groovy.sql.Sql
import disc99.hogan.parser.TableParser

class Database {

    Sql sql

    Database(Sql sql) {
        this.sql = sql
    }

    Database(String url, String driverClassName) {
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


