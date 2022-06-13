package app.service

import app.table.user.User
import app.table.user.UserEntity
import org.jetbrains.exposed.sql.transactions.transaction

object UserService {

    fun insertUser(user: User): User {
        val insertedUser = transaction {
            UserEntity.new {
                name = user.name;
                age = user.age;
            }
        }
        return insertedUser.toObject();
    }

    fun getAllUser(): List<User> {
        val listUser = transaction {
            UserEntity.all().map { it.toObject() }.toList();
        }
        return listUser;
    }
}