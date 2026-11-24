def s = vars.getObject('lab2')
s.runStep(SampleResult, 'CRUD') {
    def created = s.decode(s.expect(s.request('POST', 'labworks',
            s.work("own-${s.runId}-${s.thread}")), [201], 'create'))
    def updated = s.decode(s.expect(s.request('PUT', "labworks/${created.id}",
            s.work(created.name, [version: created.version, description: 'edited'])),
            [200], 'update'))
    s.expect(s.request('DELETE', "labworks/${created.id}?version=${updated.version}"),
            [204], 'delete')
}
