def s = vars.getObject('lab2')
s.runStep(SampleResult, 'unique coordinates') {
    def prefix = "coordinates-${s.runId}"
    def point = [x: 20000.5, y: Math.floorMod(prefix.hashCode(), 2000000000) + 1]
    s.barrier('before-coordinates')
    def response = s.request('POST', 'labworks',
            s.work("${prefix}-${s.thread}", [coordinates: point]))
    s.statuses('coordinates', response.code)
    s.barrier('after-coordinates')
    s.checkStatuses('coordinates', 201, [409])
    if (s.countNamed(prefix) != 1) {
        throw new AssertionError('The coordinate pair must be used by exactly one work')
    }
    s.barrier('coordinates-counted')
    if (s.thread == 0) {
        s.namedWorks(prefix).each {
            s.expect(s.request('DELETE', "labworks/${it.id}?version=${it.version}"),
                    [204], 'coordinate cleanup')
        }
    }
    s.barrier('coordinates-cleanup')

    def first = s.decode(s.expect(s.request('POST', 'labworks',
            s.work("coord-update-a-${s.runId}-${s.thread}")), [201], 'coordinate update first'))
    def second = s.decode(s.expect(s.request('POST', 'labworks',
            s.work("coord-update-b-${s.runId}-${s.thread}")), [201], 'coordinate update second'))
    s.expect(s.request('PUT', "labworks/${second.id}",
            s.work(second.name, [version: second.version, coordinates: first.coordinates])),
            [409], 'duplicate coordinates on update')
    s.expect(s.request('DELETE', "labworks/${second.id}?version=${second.version}"),
            [204], 'coordinate update second cleanup')
    s.expect(s.request('DELETE', "labworks/${first.id}?version=${first.version}"),
            [204], 'coordinate update first cleanup')
}
