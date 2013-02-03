package com.jagielu.vertx

import org.vertx.java.core.json.JsonObject

def eb = vertx.eventBus

def collector = new StatsCollector()

def address = 'web.client'

// Send a message every 2 seconds
vertx.setPeriodic(2000) {
    def msg = new JsonObject(collector.collectStats())
    eb.send(address, msg)
}
