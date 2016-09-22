package disc99.hogan.parser

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
        actual.toString() == '[disc99.hogan.parser.Row([disc99.hogan.parser.Column(name), disc99.hogan.parser.Column(age), disc99.hogan.parser.Column(married), disc99.hogan.parser.Column(weight), disc99.hogan.parser.Column(birthday)]), disc99.hogan.parser.Row([Tom, 20, false, 75.6, 1995-01-01]), disc99.hogan.parser.Row([Chris, 30, true, 42.3, 1985-12-31])]'
    }

    def "data to table"() {
        setup:
        def actual = TableParser.asTable dataTable

        expect:
        actual.toString() == 'disc99.hogan.parser.Table([disc99.hogan.parser.Column(name), disc99.hogan.parser.Column(age), disc99.hogan.parser.Column(married), disc99.hogan.parser.Column(weight), disc99.hogan.parser.Column(birthday)], [disc99.hogan.parser.Row([Tom, 20, false, 75.6, 1995-01-01]), disc99.hogan.parser.Row([Chris, 30, true, 42.3, 1985-12-31])])'
    }

    def "data to map list"() {
        setup:
        def actual = TableParser.asTable(dataTable).toMapList()

        expect:
        actual.toString() == '[[name:Tom, age:20, married:false, weight:75.6, birthday:1995-01-01], [name:Chris, age:30, married:true, weight:42.3, birthday:1985-12-31]]'
    }
}
