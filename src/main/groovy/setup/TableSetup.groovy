package setup

import groovy.sql.Sql
import parser.TableParser

class TableSetup {

    def static insert(Sql sql, @DelegatesTo(TableInsertHandler) Closure... cls) {
        cls.each {
            def tableInsert = new TableInsertHandler(sql: sql)
            def handler = it.rehydrate(tableInsert, this, this)
            handler.resolveStrategy = Closure.DELEGATE_ONLY
            handler()
        }
    }

    static class TableInsertHandler {
        Sql sql
        String table
        def table(String table) { this.table = table }
        def rows(Closure cl) {
            TableParser.asTable(cl).toMapList().each {
                sql.dataSet(table).add(it)
            }
        }
    }
}


