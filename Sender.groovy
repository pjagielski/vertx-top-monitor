import org.vertx.java.core.json.JsonObject

def eb = vertx.eventBus

def address = 'web.client'

def count = 0

// Send a message every 2 seconds
vertx.setPeriodic(2000) {
    def msg = new JsonObject(getStats())
    eb.send(address, msg)
}

def getStats() {
    def cmd = "top -b -n 1".execute()
    cmd.waitFor()
    def stats = [:]
    List<String> lines = cmd.in.newReader().readLines()
    stats.tasks = lines[1][6..9] as Long
    stats.avgCpu = lines[2][7..11] as BigDecimal
    stats.memUsed = lines[3][23..30] as Long
    stats.memFree = lines[3][39..46] as Long
    stats.swapUsed = lines[4][23..30] as Long
    stats.swapFree = lines[4][39..46] as Long
    stats.topProc = lines[7..17].collect { String line ->
        [
            pid:line[0..5] as Long, user:line[6..15].trim(),
            cpu:line[42..45] as Long, mem:line[46..49] as BigDecimal,
            command:line[61..line.length()-1].trim()
        ]
    }
    return stats
}

