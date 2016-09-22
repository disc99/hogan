package disc99.hogan.parser

import groovy.transform.ToString
import groovy.transform.TupleConstructor

@ToString
@TupleConstructor
class Table {
    List columns
    List rows

    def toMapList() {
        rows.collect { [columns, it.values].transpose().collectEntries { [it[0].name, it[1]] } }
    }
}
