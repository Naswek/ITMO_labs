def s = vars.getObject('lab2')
s.runStep(SampleResult, 'CSV import and rollback') {
    def prefix = "import-${s.runId}-u${s.thread}-"
    def imported = [s.work("${prefix}1", [
            discipline: [name: "Discipline-${s.runId}-${s.thread}",
                         practiceHours: 12, selfStudyHours: 20, labsCount: 2],
            author: [name: "Author-${s.runId}-${s.thread}", eyeColor: 'GREEN',
                     nationality: 'SPAIN', birthday: '2000-01-02T00:00:00Z',
                     location: [x: 1, y: 2.5, z: 3, name: 'Madrid']]]),
            s.work("${prefix}2")]
    def operation = s.decode(s.expect(s.request('POST', 'imports',
            s.csvFile(imported), 'text/csv'), [201], 'valid import'))
    if (operation.status != 'SUCCESS' || operation.addedCount != 2 || s.countNamed(prefix) != 2) {
        throw new AssertionError('Successful import did not add exactly two works')
    }

    def pages = "labworks?filterField=name&filterValue=${prefix}&sortField=name&direction=asc"
    def first = s.decode(s.expect(s.request('GET', pages + '&page=0&size=1'),
            [200], 'first page'))
    def second = s.decode(s.expect(s.request('GET', pages + '&page=1&size=1'),
            [200], 'second page'))
    if (first.total != 2 || second.total != 2 || first.items.size() != 1
            || second.items.size() != 1 || first.items[0].id == second.items[0].id) {
        throw new AssertionError('Pagination did not return two different works')
    }

    def badPrefix = "bad-${s.runId}-${s.thread}"
    def invalid = [s.work("${badPrefix}-1"), s.work("${badPrefix}-2", [averagePoint: 0])]
    s.expect(s.request('POST', 'imports', s.csvFile(invalid), 'text/csv'),
            [400], 'invalid import')
    if (s.countNamed(badPrefix) != 0) {
        throw new AssertionError('Failed import left a partial work behind')
    }

    def history = s.decode(s.expect(s.request('GET', 'imports'), [200], 'import history'))
    if (!history.any { it.id == operation.id && it.status == 'SUCCESS' && it.addedCount == 2 }) {
        throw new AssertionError('Successful import is missing from history')
    }
    if (!history.any { it.status == 'FAILED' && it.addedCount == null }) {
        throw new AssertionError('Failed import is missing from history')
    }
    if (history.any { it.user != s.login }) {
        throw new AssertionError('Ordinary user can see another user\'s history')
    }
}
