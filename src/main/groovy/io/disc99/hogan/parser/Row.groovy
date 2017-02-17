package io.disc99.hogan.parser

import groovy.transform.ToString

@ToString
class Row {

    List values = []

    Row or(arg) {
        values.add(arg)
        this
    }
}
