def s = vars.getObject('lab2')
s.runStep(SampleResult, 'unique names') {
    def prefix = "import-${s.runId}-u${s.thread}-"
    def target = s.decode(s.expect(s.request('POST', 'labworks',
            s.work("rename-${s.runId}-${s.thread}")), [201], 'rename target create'))
    s.expect(s.request('PUT', "labworks/${target.id}",
            s.work("${prefix}1", [version: target.version, coordinates: target.coordinates])),
            [409], 'duplicate rename')
    s.expect(s.request('DELETE', "labworks/${target.id}?version=${target.version}"),
            [204], 'rename target cleanup')

    s.barrier('before-unique')
    def uniqueName = "unique-${s.runId}"
    def created = s.request('POST', 'labworks', s.work(uniqueName))
    s.statuses('unique', created.code)
    s.barrier('after-unique')
    s.checkStatuses('unique', 201, [409])
    if (s.countNamed(uniqueName) != 1) {
        throw new AssertionError('The unique name has other than one work')
    }
    s.barrier('unique-counted')
    if (s.thread == 0) {
        s.namedWorks(uniqueName).each {
            s.expect(s.request('DELETE', "labworks/${it.id}?version=${it.version}"),
                    [204], 'unique cleanup')
        }
    }
    s.barrier('unique-cleanup')
}
