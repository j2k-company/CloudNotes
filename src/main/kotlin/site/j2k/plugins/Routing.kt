package site.j2k.plugins

import io.ktor.server.routing.*
import io.ktor.server.application.*
import site.j2k.routers.notes

fun Application.configureRouting() {
    routing {
        notes()
    }
}
