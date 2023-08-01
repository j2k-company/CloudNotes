package site.j2k.routers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.and
import site.j2k.JWTFactory
import site.j2k.models.*

fun Route.users() {
    route("users") {
        login()
        register()
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

fun Route.register() {
    post("/register") {
        val user = call.receive<UserLoginRequest>()

        val users = User.find { Users.username eq user.username }

        if (isValidNewUserData(user)) {
            call.respond(HttpStatusCode.BadRequest, "Username or password is invalid")
            return@post
        }
        if (!users.empty()) {
            call.respond(HttpStatusCode.Conflict, "User with this username already exists")
            return@post
        }

        User.new {
            username = user.username
            password = user.password
        }

        call.respond(HttpStatusCode.NoContent, "Registration was successful")
    }
}

fun isValidNewUserData(user: UserLoginRequest): Boolean =
    !(user.username.isEmpty() || user.username.length > USERNAME_LENGTH
            || user.password.isEmpty() || user.password.length > PASSWORD_LENGTH)
