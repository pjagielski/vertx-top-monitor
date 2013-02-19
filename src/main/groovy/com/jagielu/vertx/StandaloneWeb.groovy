package com.jagielu.vertx

import org.vertx.java.core.Handler
import org.vertx.java.core.http.HttpServerRequest
import org.vertx.java.core.http.RouteMatcher
import org.vertx.java.core.json.JsonArray
import org.vertx.java.core.json.JsonObject
import org.vertx.java.core.logging.Logger
import org.vertx.java.core.logging.impl.Log4jLogDelegate
import org.vertx.java.core.sockjs.SockJSServer
import org.vertx.java.deploy.Verticle

class StandaloneWeb extends Verticle {

    def logger = new Logger(new Log4jLogDelegate("com.jagielu"))

    @Override
    void start() throws Exception {

        def rm = new RouteMatcher()
        rm.get('/details/:user/:id', { HttpServerRequest req ->
            req.response.end "User: ${req.params()['user']} ID: ${req.params()['id']}"
        } as Handler<HttpServerRequest>)

        rm.get('/', { HttpServerRequest req ->
            req.response.sendFile("web/index.html")
        } as Handler<HttpServerRequest>)

        // Catch all - serve the index page
        rm.getWithRegEx('.*', { HttpServerRequest req ->
            req.response.sendFile "web/${req.path}"
        } as Handler<HttpServerRequest>)

        def server = vertx.createHttpServer()
        server.requestHandler(rm)

        logger.info("Server created")

        SockJSServer sjsServer = vertx.createSockJSServer(server)

        sjsServer.bridge(new JsonObject().putString("prefix", "/eventbus"),
                         new JsonArray(),
                         new JsonArray().add(new JsonObject().putString("address", "web.client")),
                         5 * 60 * 1000)

        logger.info("SockJS bridge created")

        server.listen(8080)

        logger.info("Server listening")

        container.deployVerticle("com.jagielu.vertx.StatsSender")
    }

}
