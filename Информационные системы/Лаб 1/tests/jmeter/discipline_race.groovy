def s = vars.getObject('lab2')
s.runStep(SampleResult, 'unique discipline names') {
    def name = "race-discipline-${s.runId}"
    def variant = s.thread == 0 ? name : " ${name.toUpperCase()} "
    s.barrier('before-discipline')
    def response = s.request('POST', 'references/disciplines',
            [name: variant, practiceHours: 12, selfStudyHours: 20, labsCount: 2])
    s.statuses('discipline', response.code)
    s.barrier('after-discipline')
    s.checkStatuses('discipline', 201, [409])
    def all = s.decode(s.expect(s.request('GET', 'references/disciplines'),
            [200], 'discipline list'))
    def matches = all.findAll { it.name.strip().equalsIgnoreCase(name) }
    if (matches.size() != 1) {
        throw new AssertionError('Normalized discipline name must identify exactly one discipline')
    }
    s.barrier('discipline-counted')
    if (s.thread == 0) {
        s.expect(s.request('DELETE', "references/disciplines/${matches[0].id}"),
                [204], 'discipline cleanup')
    }
    s.barrier('discipline-cleanup')

    def first = s.decode(s.expect(s.request('POST', 'references/disciplines',
            [name: "discipline-update-a-${s.runId}-${s.thread}",
             practiceHours: 1, selfStudyHours: 2]), [201], 'discipline update first'))
    def second = s.decode(s.expect(s.request('POST', 'references/disciplines',
            [name: "discipline-update-b-${s.runId}-${s.thread}",
             practiceHours: 1, selfStudyHours: 2]), [201], 'discipline update second'))
    s.expect(s.request('PUT', "references/disciplines/${second.id}",
            [name: " ${first.name.toUpperCase()} ", practiceHours: 1, selfStudyHours: 2]),
            [409], 'duplicate discipline name on update')
    s.expect(s.request('DELETE', "references/disciplines/${second.id}"),
            [204], 'discipline update second cleanup')
    s.expect(s.request('DELETE', "references/disciplines/${first.id}"),
            [204], 'discipline update first cleanup')
}
