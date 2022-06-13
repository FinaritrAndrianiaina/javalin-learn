package app.table.todo

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class TodoEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TodoEntity>(TodosTable);
    var task by TodosTable.task
    var done by TodosTable.done
}