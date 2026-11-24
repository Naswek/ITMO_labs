def s = vars.getObject('lab2')
s.runStep(SampleResult, 'concurrent update') {
    s.barrier('before-update')
    def updated = s.request('PUT', "labworks/${s.targetId}",
            s.work("shared-${s.runId}",
                    [version: s.oldVersion, description: "changed by ${s.thread}"]))
    s.statuses('update', updated.code)
    s.barrier('after-update')
    s.checkStatuses('update', 200, [409])
}
