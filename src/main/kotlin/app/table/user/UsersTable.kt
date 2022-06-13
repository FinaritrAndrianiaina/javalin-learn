package app.table.user;

import org.jetbrains.exposed.dao.id.IntIdTable

object UsersTable : IntIdTable() {
    val name = varchar("name", 50);
    val age = integer("age").default(0);
}

