def s = vars.getObject('lab2')
s.runStep(SampleResult, 'cleanup') {
    s.namedWorks("import-${s.runId}-u${s.thread}-").each {
        s.expect(s.request('DELETE', "labworks/${it.id}?version=${it.version}"),
                [204], 'import cleanup')
    }
}
