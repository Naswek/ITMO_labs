def s = vars.getObject('lab2')
s.runStep(SampleResult, 'concurrent delete') {
    def latestVersion = s.decode(s.expect(s.request('GET', "labworks/${s.targetId}"),
            [200], 'get latest')).version
    s.barrier('before-delete')
    def deleted = s.request('DELETE', "labworks/${s.targetId}?version=${latestVersion}")
    s.statuses('delete', deleted.code)
    s.barrier('after-delete')
    s.checkStatuses('delete', 204, [404, 409])
}
