package app.service

import app.security.UserJwt
import app.table.user.User
import app.table.user.UserEntity
import app.table.user.UsersTable
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

object UserService {

    fun insertUser(user: User): User {
        val insertedUser = transaction {
            UserEntity.new {
                username = user.username;
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

    fun findWithUsernameAndPassword(username: String, password: String): UserEntity? {
        val userFound = transaction {
            UserEntity.find { (UsersTable.username eq username) and (UsersTable.password eq password) }.firstOrNull()
        }
        return userFound;
    }
}