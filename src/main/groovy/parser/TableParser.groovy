package parser

class TableParser {

    private static ThreadLocal<List> context = new ThreadLocal<List>()

    static or(self, arg) {
        appendRow(self, arg)
    }

    static appendRow(value, nextValue) {
        def row = new Row(values: [value])
        context.get().add(row)
        row.or(nextValue)
    }

    static asListOfRows(Closure tableData) {
        context.set([])
        use(TableParser) {
            tableData.delegate = new PropertyColumnConverter()
            tableData.resolveStrategy = Closure.DELEGATE_FIRST
            tableData()
        }
        context.get()
    }

    static asTable(Closure tableData) {
        def list = asListOfRows(tableData)
        new Table(list.head().values, list.tail())
    }
}
