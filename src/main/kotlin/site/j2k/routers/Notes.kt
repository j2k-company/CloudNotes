// TODO: create NoteService and move CRUD operations to it
package site.j2k.routers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.dao.exceptions.EntityNotFoundException
import site.j2k.models.Note
import site.j2k.models.NoteRequest
import site.j2k.models.NoteResponse
import site.j2k.models.User

fun Route.notes() {
    route("notes/") {
        authenticate {
            getNotes()
            createNotes()
            updateNotes()
            deleteNotes()
        }
    }
}

fun Route.getNotes() {
    get {
//        notes.getSeq()
    }
    get("/{id}") {
        val note = getNote(call) ?: return@get
        val user = getUser(call.principal<JWTPrincipal>()!!)

        if (note.user.id.value != user.id.value) {
            call.respond(HttpStatusCode.Forbidden, "No access to this note")
            return@get
        }

        call.respond(HttpStatusCode.OK, note.asResponse())
    }
}

fun Route.createNotes() {
    post {
        val user = getUser(call.principal<JWTPrincipal>()!!)

        val note = call.receive<NoteResponse>()
        val id = Note.new {
            this.user = user
            this.title = note.title
            this.text = note.text
        }

        call.response.headers.append("Location", "/notes/$id")
        call.respond(HttpStatusCode.Created)
    }
}

fun Route.updateNotes() {
    put("/{id}") {
        val note = getNote(call) ?: return@put
        val user = getUser(call.principal<JWTPrincipal>()!!)

        if (note.user.id.value != user.id.value) {
            call.respond(HttpStatusCode.Forbidden, "No access to note with id ${note.id.value}")
            return@put
        }

        val newNote = call.receive<NoteRequest>()
        note.title = newNote.title
        note.text = newNote.text

        call.response.headers.append("Content-Location", "/notes/${note.id.value}")
        call.respond(HttpStatusCode.OK)
    }
    patch("/{id}") {
        val id = getNoteID(call) ?: return@patch

    }
}

fun Route.deleteNotes() {
    delete("/{id}") {
        val note = getNote(call) ?: return@delete
        val user = getUser(call.principal<JWTPrincipal>()!!)

        if (note.user.id.value != user.id.value) {
            call.respond(HttpStatusCode.Forbidden, "No access to note with id ${note.id.value}")
            return@delete
        }

        note.delete()

        call.respond(HttpStatusCode.NoContent)
    }
}

suspend fun getNoteID(call: ApplicationCall): Int? {
    var id: Int? = null
    try {
        id = call.parameters.getOrFail("id").toInt()
    } catch (_: MissingRequestParameterException) {
        call.respond(HttpStatusCode.BadRequest, "Note ID not specified.")
    } catch (_: NumberFormatException) {
        call.respond(HttpStatusCode.BadRequest, "Note ID must be a number.")
    }
    return id
}

suspend fun getNote(call: ApplicationCall): Note? {
    val id = getNoteID(call) ?: return null
    val note: Note
    try {
        note = Note[id]
    } catch (_: EntityNotFoundException) {
        call.respond(HttpStatusCode.NotFound, "Note with id $id not found")
        return null
    }
    return note
}

fun getUser(principal: JWTPrincipal) = User.get(principal.payload.getClaim("user_id").asInt())

