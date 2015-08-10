package parser

class PropertyColumnConverter {

    def getProperty(String property) {
        new Column(name: property)
    }
}
