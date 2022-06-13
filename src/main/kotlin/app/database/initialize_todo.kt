package app.database

import app.table.todo.TodoEntity
import app.table.todo.TodosTable
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    DatabaseConnection.connect();
    transaction {
        if (!TodosTable.exists()) {
            SchemaUtils.create(TodosTable);
            TodoEntity.new {
                task = "Insérer des données"
            }
            TodoEntity.new {
                task = "Trouver des données"
            }
            TodoEntity.new {
                task = "Ajouter la base de donnée"
                done = false;
            }
        }
        println("${TodosTable.exists()} Todo Exists")
    }
}