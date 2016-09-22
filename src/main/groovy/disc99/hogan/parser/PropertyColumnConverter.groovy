package disc99.hogan.parser

class PropertyColumnConverter {

    def getProperty(String property) {
        new Column(name: property)
    }
}
