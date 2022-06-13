package app.service

import app.security.UserJwt
import app.table.user.User
import app.table.user.UserEntity
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

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

    fun getUserById(id: Int): User {
        val foundUser = transaction {
            UserEntity.findById(id)
        }
        return Optional
            .ofNullable(foundUser)
            .orElseThrow { throw Exception("User Entity Not Found") }
            .toObject();
    }


    fun generateToken(id: Int): String {
        val userFound = getUserById(id);
        val jwtEncoded = UserJwt(userFound).encoded;
        return jwtEncoded;
    }

    fun getAllUser(): List<User> {
        val listUser = transaction {
            UserEntity.all().map { it.toObject() }.toList();
        }
        return listUser;
    }
}