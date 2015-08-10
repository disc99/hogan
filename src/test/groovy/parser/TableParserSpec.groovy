package parser

import spock.lang.Specification


class TableParserSpec extends Specification {

    static def dataTable

    def setupSpec() {
        dataTable = {
            name     | age | married | weight | birthday
            'Tom'    | 20  | false   | 75.6   | '1995-01-01'
            'Chris'  | 30  | true    | 42.3   | '1985-12-31'
        }
    }

    def "data to list"() {
        setup:
        def actual = TableParser.asListOfRows dataTable

        expect:
        actual.toString() == '[parser.Row([parser.Column(name), parser.Column(age), parser.Column(married), parser.Column(weight), parser.Column(birthday)]), parser.Row([Tom, 20, false, 75.6, 1995-01-01]), parser.Row([Chris, 30, true, 42.3, 1985-12-31])]'
    }

    def "data to table"() {
        setup:
        def actual = TableParser.asTable dataTable

        expect:
        actual.toString() == 'parser.Table([parser.Column(name), parser.Column(age), parser.Column(married), parser.Column(weight), parser.Column(birthday)], [parser.Row([Tom, 20, false, 75.6, 1995-01-01]), parser.Row([Chris, 30, true, 42.3, 1985-12-31])])'
    }

    def "data to map list"() {

        setup:
        def actual = TableParser.asTable(dataTable).toMapList()

        expect:
        actual.toString() == '[[name:Tom, age:20, married:false, weight:75.6, birthday:1995-01-01], [name:Chris, age:30, married:true, weight:42.3, birthday:1985-12-31]]'
    }
}
