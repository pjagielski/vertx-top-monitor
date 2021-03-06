package com.jagielu.vertx

import org.vertx.java.core.json.JsonObject
import org.vertx.java.deploy.Verticle

class WebApp extends Verticle {

    def webServerConf = [

      // Normal web server stuff
      port: 8080,
      ssl: false,

      // Configuration for the event bus client side bridge
      // This bridges messages from the client side to the server side event bus
      bridge: true,

      // This defines which messages from the client we will let through
      // to the server side
      inbound_permitted: [
        [:]
      ],

      // This defines which messages from the server we will let through to the client
      outbound_permitted: [
        [address: 'web.client']
      ]
    ]

    @Override
    void start() throws Exception {
        container.with {
            // Start the web server, with the config we defined above
            deployModule('vertx.web-server-v1.0', new JsonObject(webServerConf))

            deployVerticle('com.jagielu.vertx.StatsSender')
        }

    }
}
