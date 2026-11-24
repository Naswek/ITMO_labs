def s = vars.getObject('lab2')
s.runStep(SampleResult, 'duplicate CSV import') {
    def prefix = "duplicate-import-${s.runId}-${s.thread}"
    s.expect(s.request('POST', 'imports',
            s.csvFile([s.work("${prefix}-1"), s.work("${prefix}-1")]),
            'text/csv'), [409], 'duplicate import')
    if (s.countNamed(prefix) != 0) {
        throw new AssertionError('Duplicate import left a partial work behind')
    }

    def coordinatePrefix = "duplicate-coordinates-${s.runId}-${s.thread}"
    def point = [x: 30000.5, y: Math.floorMod(coordinatePrefix.hashCode(), 2000000000) + 1]
    s.expect(s.request('POST', 'imports',
            s.csvFile([s.work("${coordinatePrefix}-1", [coordinates: point]),
                       s.work("${coordinatePrefix}-2", [coordinates: point])]),
            'text/csv'), [409], 'duplicate coordinates in import')
    if (s.countNamed(coordinatePrefix) != 0) {
        throw new AssertionError('Duplicate coordinates left a partial import behind')
    }

    def disciplinePrefix = "duplicate-discipline-${s.runId}-${s.thread}"
    def discipline = [name: "D-${disciplinePrefix}", practiceHours: 1, selfStudyHours: 2]
    s.expect(s.request('POST', 'imports', s.csvFile([
            s.work("${disciplinePrefix}-1", [discipline: discipline]),
            s.work("${disciplinePrefix}-2",
                    [discipline: [name: " d-${disciplinePrefix} ",
                                  practiceHours: 1, selfStudyHours: 2]])]),
            'text/csv'), [409], 'duplicate discipline in import')
    if (s.countNamed(disciplinePrefix) != 0) {
        throw new AssertionError('Duplicate disciplines left a partial import behind')
    }
}
