package com.jagielu.vertx

class TopOutputFormat {
    def AVG_CPU, MEM_USED, MEM_FREE
}

class StatsCollector {

    def ARM_TOP_FORMAT = new TopOutputFormat(AVG_CPU: 8..13, MEM_USED: 26..33, MEM_FREE: 41..48)
    def X86_TOP_FORMAT = new TopOutputFormat(AVG_CPU: 7..11, MEM_USED: 23..30, MEM_FREE: 39..46)

    @Lazy TopOutputFormat format = isOnARM() ? ARM_TOP_FORMAT : X86_TOP_FORMAT

    def collectStats() {
        def stats = [:]
        List<String> lines = executeCommand("top -b -n 1")
        stats.tasks    = lines[1][6..9] as Long
//        stats.avgCpu   = lines[2][format.AVG_CPU] as BigDecimal
        stats.memUsed  = lines[3][format.MEM_USED] as Long
        stats.memFree  = lines[3][format.MEM_FREE] as Long
        stats.memPerc  = (stats.memUsed / (stats.memUsed + stats.memFree) * 100) as Integer
        stats.swapUsed = lines[4][format.MEM_USED] as Long
        stats.swapFree = lines[4][format.MEM_FREE] as Long
        stats.swapPerc = (stats.swapUsed / (stats.swapUsed + stats.swapFree) * 100) as Integer
        stats.topProc  = lines[7..17].collect { String line -> [
            pid:     line[0..5] as Long,
            user:    line[6..15].trim(),
            cpu:     line[42..45] as BigDecimal,
            mem:     line[46..49] as BigDecimal,
            command: line[61..line.length()-1].trim()
        ]}
        stats.avgCpu = stats.topProc.sum { it.cpu }
        return stats
    }

    private List<String> executeCommand(String command) {
        def cmd = command.execute()
        cmd.waitFor()
        return cmd.in.newReader().readLines().collect { it.replaceAll(",",".") }
    }

    boolean isOnARM() {
        return executeCommand("uname -a").get(0).contains("arm")
    }

}
