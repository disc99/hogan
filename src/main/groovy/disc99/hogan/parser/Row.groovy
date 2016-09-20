package disc99.hogan.parser

import groovy.transform.ToString

@ToString
class Row {

    List values = []

    def or(arg) {
        values.add(arg)
        this
    }
}
