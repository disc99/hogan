package io.disc99.hogan.parser

class PropertyColumnConverter {

    Column getProperty(String property) {
        new Column(name: property)
    }
}
