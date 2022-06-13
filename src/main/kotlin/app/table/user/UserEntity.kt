package app.table.user

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(UsersTable)

    var name by UsersTable.name
    var age by UsersTable.age

    fun toObject(): User {
        return User(this.id.value, name, age)
    }
}
