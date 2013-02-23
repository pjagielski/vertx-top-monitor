package com.jagielu.vertx

import org.junit.Test

import static org.junit.Assert.*

class StatsCollectorTest {

    StatsCollector statsCollector = new StatsCollector()

    @Test
    void shouldParseTopOnX86() {
        statsCollector.metaClass.executeCommand = { String cmd ->
            this.class.getResourceAsStream('/top-x86.txt').readLines()
        }
        def stats = statsCollector.collectStats()
        assertEquals(231, stats.tasks)
        assertEquals(14.0, stats.avgCpu, 0)
    }

    @Test
    void shouldParseTopOnARM() {
        statsCollector.metaClass.executeCommand = { String cmd ->
            this.class.getResourceAsStream('/top-ARM.txt').readLines().collect { it.replaceAll(",",".") }
        }
        statsCollector.metaClass.isOnARM = { true }
        def stats = statsCollector.collectStats()
        assertEquals(55, stats.tasks)
        assertEquals(11.5, stats.avgCpu, 0)
    }

}
