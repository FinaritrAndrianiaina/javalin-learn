package app.database

import app.table.user.UserEntity
import app.table.user.UsersTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    DatabaseConnection.connect();
    transaction {
        if (!UsersTable.exists()) {
            SchemaUtils.create(UsersTable);
            UserEntity.new {
                username = "Garret";
                age = 32;
            }
            UserEntity.new {
                username = "Alfred";
                age = 10;
            }
        } else {
            SchemaUtils.createMissingTablesAndColumns(UsersTable, inBatch = true, withLogs = true)
        }
    }
}