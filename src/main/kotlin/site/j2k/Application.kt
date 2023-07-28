package site.j2k

import io.ktor.server.application.*
import site.j2k.plugins.*

const val TOKEN_LIFETIME = 1_200_000

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    configureDatabase()
    configureRouting()
    configureSerialization()
    configureAuthentication()
}
