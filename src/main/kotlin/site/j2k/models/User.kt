package site.j2k.models

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object Users : IntIdTable() {
    var username: Column<String> = varchar("username", 25).uniqueIndex()
    var password: Column<String> = varchar("password", 35)
}

class User(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<User>(Users)
    val notes    by Note referrersOn Notes.user
    var username by Users.username
    var password by Users.password
}
