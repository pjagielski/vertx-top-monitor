package com.jagielu.vertx

import org.vertx.java.core.Handler
import org.vertx.java.core.json.JsonObject
import org.vertx.java.core.logging.Logger
import org.vertx.java.core.logging.impl.Log4jLogDelegate
import org.vertx.java.deploy.Verticle

class StatsSender extends Verticle {

    def logger = new Logger(new Log4jLogDelegate("com.jagielu"))

    def address = "web.client"
    def collector = new StatsCollector()

    @Override
    void start() throws Exception {
        vertx.setPeriodic(2000, {
            def msg = new JsonObject(collector.collectStats())
            vertx.eventBus().send(address, msg)
        } as Handler<Long>)

        logger.info("StatsSender started")
    }
}