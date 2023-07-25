package site.j2k.models

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object Notes : IntIdTable() {
    val user = reference("user", Users)
    val title: Column<String> = varchar("title", 60)
    val text: Column<String?> = text("text").nullable()
}

class Note(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Note>(Notes)
    val user  by User referencedOn Notes.user
    var title by Notes.title
    var text  by Notes.text
}
