package site.j2k.routers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.and
import site.j2k.JWTFactory
import site.j2k.models.User
import site.j2k.models.UserLoginRequest
import site.j2k.models.Users

fun Route.users() {
    route("users") {
        login()
    }
}

fun Route.login() {
    post("/login") {
        val user = call.receive<UserLoginRequest>()

        val users = User.find {
            (Users.username eq user.username) and (Users.password eq user.password)
        }

        if (users.empty()) {
            call.respond(HttpStatusCode.Unauthorized, "Username or password is not valid")
            return@post
        }

        val token = JWTFactory.getToken(users.first().id.value)
        call.respond(HttpStatusCode.OK, hashMapOf("token" to token))
    }
}
