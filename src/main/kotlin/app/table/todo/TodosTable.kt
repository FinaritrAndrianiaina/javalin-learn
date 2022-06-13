package app.table.todo;

import org.jetbrains.exposed.dao.id.IntIdTable


object TodosTable : IntIdTable() {
    val task = varchar("task", 50);
    val done = bool("done").default(false);

}

