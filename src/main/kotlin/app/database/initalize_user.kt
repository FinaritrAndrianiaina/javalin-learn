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
                name = "Garret";
                age = 32;
            }
            UserEntity.new {
                name = "Alfred";
                age = 10;
            }
        }
    }
}