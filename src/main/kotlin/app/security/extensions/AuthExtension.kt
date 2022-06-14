package app.security.extensions

import app.security.Role
import app.service.UserService
import app.table.user.UserEntity
import app.table.user.UsersTable
import io.javalin.http.Context
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

val Context.user: Optional<UserEntity>
    get() {
        return if (basicAuthCredentialsExist()) {
            val auth = basicAuthCredentials();
            val userFound = UserService.findWithUsernameAndPassword(auth.username, auth.password);
            Optional.ofNullable(userFound);
        } else {
            Optional.empty();
        }
    }

val Context.userRoles: Set<Role>
    get() {
        return if (this.user.isPresent) {
            setOf(Role.USER)
        } else {
            emptySet()
        }
    }