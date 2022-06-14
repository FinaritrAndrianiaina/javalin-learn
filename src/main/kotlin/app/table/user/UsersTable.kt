package app.table.user;

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.QueryBuilder

object UsersTable : IntIdTable() {
    val password = varchar(name = "password", 50);
    val username = varchar("username", 50);
    val age = integer("age").default(0);

}

