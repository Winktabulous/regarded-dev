package com.near_reality.api

import com.near_reality.api.service.sanction.SanctionAPIService
import com.near_reality.api.service.store.StoreAPIService
import com.near_reality.api.service.vote.VoteAPIService
import com.zenyte.game.world.World
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory
import org.slf4j.event.Level

object APICallbackServer {

    private val logger = LoggerFactory.getLogger(this::class.java.simpleName)

    fun start(callbackServerPort: Int = 6969): Boolean {
        try {
            embeddedServer(Netty, port = callbackServerPort) {
                install(ContentNegotiation) {
                    json()
                }
                install(CallLogging) {
                    level = Level.TRACE
                }
                install(Resources)
                routing {
                    with(StoreAPIService) {
                        orderCallback()
                    }
                    with(VoteAPIService) {
                        voteCallback()
                    }
                    with(SanctionAPIService) {
                        sanctionCallback()
                    }
                    get("/shutmedownplease") {
                        World.shutdown()
                    }
                }
            }.start(wait = false)
            logger.info("Store API callback server started on port {}", callbackServerPort)
            return true
        } catch (e: Exception) {
            logger.error("Failed to start store API callback server", e)
            return false
        }
    }
}
