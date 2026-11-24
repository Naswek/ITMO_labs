def s = vars.getObject('lab2')
s.runStep(SampleResult, 'register and login') {
    def login = "lab2-${s.runId}-${s.thread}"
    def credentials = [login: login, password: 'lab2-test-password']
    s.expect(s.request('POST', 'users', credentials), [201], 'register')
    s.expect(s.request('POST', 'users/login', credentials), [200], 'login')
    s.login = login

    if (s.thread == 0) {
        def created = s.expect(s.request('POST', 'labworks', s.work("shared-${s.runId}")),
                [201], 'shared create')
        props.put('lab2.target.' + s.runId, s.decode(created).id.toString())
    }
    s.barrier('target-created')
    s.targetId = props.get('lab2.target.' + s.runId)
    s.oldVersion = s.decode(s.expect(s.request('GET', "labworks/${s.targetId}"),
            [200], 'shared get')).version
}
