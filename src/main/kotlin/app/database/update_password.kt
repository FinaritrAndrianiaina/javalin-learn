package app.database

import app.table.user.UsersTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    DatabaseConnection.connect();
    transaction {
        UsersTable.update({ UsersTable.id greaterEq 0 }) {
            it[password] = "admin"
        }
    }
}