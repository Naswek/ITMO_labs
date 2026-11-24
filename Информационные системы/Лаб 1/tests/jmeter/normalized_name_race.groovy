def s = vars.getObject('lab2')
s.runStep(SampleResult, 'normalized work names') {
    def prefix = "normalized-${s.runId}"
    def name = s.thread == 0 ? prefix : " ${prefix.toUpperCase()} "
    s.barrier('before-normalized-name')
    def response = s.request('POST', 'labworks', s.work(name))
    s.statuses('normalized-name', response.code)
    s.barrier('after-normalized-name')
    s.checkStatuses('normalized-name', 201, [409])
    if (s.countNamed(prefix) != 1) {
        throw new AssertionError('Normalized name must identify exactly one work')
    }
    s.barrier('normalized-name-counted')
    if (s.thread == 0) {
        s.namedWorks(prefix).each {
            s.expect(s.request('DELETE', "labworks/${it.id}?version=${it.version}"),
                    [204], 'normalized name cleanup')
        }
    }
    s.barrier('normalized-name-cleanup')

    def first = s.decode(s.expect(s.request('POST', 'labworks',
            s.work("name-update-a-${s.runId}-${s.thread}")), [201], 'name update first'))
    def second = s.decode(s.expect(s.request('POST', 'labworks',
            s.work("name-update-b-${s.runId}-${s.thread}")), [201], 'name update second'))
    s.expect(s.request('PUT', "labworks/${second.id}",
            s.work(" ${first.name.toUpperCase()} ",
                    [version: second.version, coordinates: second.coordinates])),
            [409], 'duplicate normalized name on update')
    s.expect(s.request('DELETE', "labworks/${second.id}?version=${second.version}"),
            [204], 'name update second cleanup')
    s.expect(s.request('DELETE', "labworks/${first.id}?version=${first.version}"),
            [204], 'name update first cleanup')
}
