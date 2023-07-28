package site.j2k.routers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import site.j2k.ICollectionService
import site.j2k.models.Note
import java.lang.NumberFormatException

lateinit var notes: ICollectionService<Note>

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
        notes.getSeq()
    }
    get("/{id}") {
        val id = getNoteID(call) ?: return@get
        notes.getOne(id)
    }
}

fun Route.createNotes() {
    post {
        val note = call.receive<Note>()
        val id = notes.create(note)
        call.response.headers.append("Location", /*add service domain*/"/notes/$id")
        call.respond(HttpStatusCode.Created)
    }
}

fun Route.updateNotes() {
    put("/{id}") {
        val id = getNoteID(call) ?: return@put
        val newNote = call.receive<Note>()
        notes.update(id, newNote)
        call.response.headers.append("Content-Location", "/notes/$id")
    }
    patch("/{id}") {
        val id = getNoteID(call) ?: return@patch

    }
}

fun Route.deleteNotes() {
    delete("/{id}") {
        val id = getNoteID(call) ?: return@delete
        if (notes.delete(id)) {
            call.respond(HttpStatusCode.NoContent)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }
}

suspend fun getNoteID(call: ApplicationCall): Long? {
    var id: Long? = null
    try {
        id = call.parameters.getOrFail("id").toLong()
    } catch (_: MissingRequestParameterException) {
        call.respond(HttpStatusCode.BadRequest, "Note ID not specified.")
    } catch (_: NumberFormatException) {
        call.respond(HttpStatusCode.BadRequest, "Note ID must be a number.")
    }
    return id
}
