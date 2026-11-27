def s = vars.getObject('lab2')
s.runStep(SampleResult, 'author and discipline pair') {
    def prefix = "author-discipline-${s.runId}"
    if (s.thread == 0) {
        def discipline = s.decode(s.expect(s.request('POST', 'references/disciplines',
                [name: "Pair discipline ${s.runId}", practiceHours: 12,
                 selfStudyHours: 20, labsCount: 2]), [201], 'create pair discipline'))
        def author = s.decode(s.expect(s.request('POST', 'references/people',
                [name: "Pair author ${s.runId}", eyeColor: 'GREEN', nationality: 'SPAIN',
                 birthday: '2000-01-02T00:00:00.000Z',
                 location: [x: 1, y: 2.5, z: 3, name: 'Madrid']]),
                [201], 'create pair author'))
        props.put('lab2.pair.discipline.' + s.runId, discipline.id.toString())
        props.put('lab2.pair.author.' + s.runId, author.id.toString())
    }
    s.barrier('pair-references-created')
    def disciplineId = props.get('lab2.pair.discipline.' + s.runId).toLong()
    def authorId = props.get('lab2.pair.author.' + s.runId).toLong()
    s.barrier('before-pair')
    def response = s.request('POST', 'labworks',
            s.work("${prefix}-${s.thread}",
                    [discipline: [id: disciplineId], author: [id: authorId]]))
    s.statuses('pair', response.code)
    s.barrier('after-pair')
    s.checkStatuses('pair', 201, [409])
    if (s.countNamed(prefix) != 1) {
        throw new AssertionError('The author and discipline pair must belong to one work')
    }
    s.barrier('pair-counted')
    if (s.thread == 0) {
        def winner = s.namedWorks(prefix)[0]
        def other = s.decode(s.expect(s.request('POST', 'labworks',
                s.work("pair-update-${s.runId}")), [201], 'pair update target'))
        s.expect(s.request('PUT', "labworks/${other.id}",
                s.work(other.name, [version: other.version,
                        discipline: [id: disciplineId], author: [id: authorId]])),
                [409], 'duplicate author and discipline on update')
        s.expect(s.request('DELETE', "labworks/${other.id}?version=${other.version}"),
                [204], 'pair update target cleanup')
        [winner].each {
            s.expect(s.request('DELETE', "labworks/${it.id}?version=${it.version}"),
                    [204], 'pair work cleanup')
        }
        s.expect(s.request('DELETE', "references/people/${authorId}"),
                [204], 'pair author cleanup')
        s.expect(s.request('DELETE', "references/disciplines/${disciplineId}"),
                [204], 'pair discipline cleanup')
    }
    s.barrier('pair-cleanup')
}
