package site.j2k

import io.ktor.server.application.*
import site.j2k.plugins.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    configureDatabase()
    configureRouting()
    configureSerialization()
}
