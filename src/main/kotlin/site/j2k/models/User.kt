package site.j2k.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

const val USERNAME_LENGTH = 25
const val PASSWORD_LENGTH = 35

object Users : IntIdTable() {
    var username: Column<String> = varchar("username", USERNAME_LENGTH).uniqueIndex()
    var password: Column<String> = varchar("password", PASSWORD_LENGTH)
}

class User(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<User>(Users)
    val notes    by Note referrersOn Notes.user
    var username by Users.username
    var password by Users.password
}

@Serializable
data class UserLoginRequest(val username: String, val password: String)
