import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import java.net.CookieManager
import java.net.CookiePolicy
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.util.concurrent.CyclicBarrier
import java.util.concurrent.TimeUnit

def base = (props.getProperty('baseUrl', 'http://localhost:8080/labworks')).replaceAll('/+$', '')
def users = props.getProperty('users', '2').toInteger()
if (users < 2) throw new IllegalArgumentException('At least two users are required')
def runId = props.computeIfAbsent('lab2.runId') {
    Long.toString(System.currentTimeMillis(), 36) + UUID.randomUUID().toString().substring(0, 5)
}
def thread = ctx.getThreadNum()
def client = HttpClient.newBuilder()
        .cookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ALL))
        .connectTimeout(Duration.ofSeconds(10)).build()
def json = new JsonSlurper()

def request = { String method, String path, Object payload = null, String contentType = 'application/json' ->
    def builder = HttpRequest.newBuilder(URI.create(base + '/api/' + path))
            .timeout(Duration.ofMillis(props.getProperty('timeoutMs', '20000').toLong()))
            .header('Accept', 'application/json')
    if (payload == null) {
        builder.method(method, HttpRequest.BodyPublishers.noBody())
    } else {
        builder.header('Content-Type', contentType)
        builder.method(method, HttpRequest.BodyPublishers.ofString(
                payload instanceof String ? payload : JsonOutput.toJson(payload), StandardCharsets.UTF_8))
    }
    def response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString())
    log.info('Lab2 [{}] {} {} -> {}', ctx.getCurrentSampler().getName(),
            method, path, response.statusCode())
    if (response.statusCode() >= 400) log.warn('Lab2 response: {}', response.body())
    [code: response.statusCode(), body: response.body()]
}
def expect = { response, codes, label ->
    if (!(response.code in codes)) throw new AssertionError(
            "${label}: expected ${codes}, got ${response.code}: ${response.body}")
    response
}
def decode = { response -> json.parseText(response.body) }
def barrier = { String phase ->
    def key = 'lab2.barrier.' + runId + '.' + phase
    CyclicBarrier value
    synchronized (props) {
        value = props.get(key) as CyclicBarrier
        if (value == null) {
            value = new CyclicBarrier(users)
            props.put(key, value)
        }
    }
    value.await(90, TimeUnit.SECONDS)
}
def statuses = { String phase, int code ->
    def key = 'lab2.status.' + runId + '.' + phase
    synchronized (props) {
        def list = props.get(key)
        if (list == null) {
            list = []
            props.put(key, list)
        }
        list.add(code)
    }
}
def checkStatuses = { String phase, int success, List<Integer> conflicts ->
    def actual = props.get('lab2.status.' + runId + '.' + phase).toList()
    if (actual.size() != users || actual.count { it == success } != 1
            || actual.any { it != success && !(it in conflicts) }) {
        throw new AssertionError(
                "${phase}: expected one ${success} and ${users - 1} of ${conflicts}, got ${actual}")
    }
}
def work = { String name, Map extra = [:] ->
    [name: name, coordinates: [x: 10000.5, y: Math.floorMod(name.hashCode(), 2000000000) + 1],
     description: 'JMeter ' + runId,
     difficulty: 'EASY', minimalPoint: 1.5, averagePoint: 2] + extra
}
def csvColumns = ['name', 'coordinates.x', 'coordinates.y', 'description', 'difficulty',
                  'minimalPoint', 'averagePoint', 'discipline.name', 'discipline.practiceHours',
                  'discipline.selfStudyHours', 'discipline.labsCount', 'author.name', 'author.eyeColor',
                  'author.hairColor', 'author.location.x', 'author.location.y', 'author.location.z',
                  'author.location.name', 'author.birthday', 'author.weight', 'author.nationality']
def csvFile = { List<Map> values ->
    def lines = values.collect { item ->
        csvColumns.collect { column ->
            def value = column.tokenize('.').inject(item) { current, key ->
                current instanceof Map ? current[key] : null
            }
            '"' + (value == null ? '' : value.toString()).replace('"', '""') + '"'
        }.join(',')
    }
    ([csvColumns.join(',')] + lines).join('\n') + '\n'
}
def namedWorks = { String prefix ->
    def response = expect(request('GET', 'labworks?filterField=name&filterValue=' + prefix),
            [200], 'list')
    decode(response).items
}
def countNamed = { String prefix -> namedWorks(prefix).size() }
def runStep = { result, String label, Closure action ->
    try {
        action()
        result.setResponseMessage(label + ' passed')
    } catch (Throwable failure) {
        result.setSuccessful(false)
        result.setResponseMessage(failure.toString())
        log.error('Lab2 ' + label + ' failed', failure)
    }
}

vars.putObject('lab2', [runId: runId, thread: thread, request: request, expect: expect,
        decode: decode, barrier: barrier, statuses: statuses, checkStatuses: checkStatuses,
        work: work, csvFile: csvFile, namedWorks: namedWorks, countNamed: countNamed,
        runStep: runStep])
SampleResult.setResponseMessage('HTTP helpers ready')
