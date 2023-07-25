package site.j2k.plugins

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import site.j2k.models.Notes
import site.j2k.models.Users

fun Application.configureDatabase() {
    val jdbcURL = environment.config.property("database.jdbc_url").getString()
    val driverClassName =
        environment.config.propertyOrNull("database.driver_class")?.getString() ?: "org.h2.Driver"

    Database.connect(jdbcURL, driver = driverClassName)

    transaction {
        SchemaUtils.create(Users, Notes)
    }
}
