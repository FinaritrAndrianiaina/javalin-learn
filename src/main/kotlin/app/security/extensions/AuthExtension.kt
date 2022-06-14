package app.security.extensions

import app.security.Role
import io.javalin.http.Context

val Context.userRoles: Set<Role>
    get() {
        return if (basicAuthCredentialsExist()) {
            basicAuthCredentials().let { (username, password) -> println("$username $password") };
            setOf(Role.USER)
        } else {
            emptySet()
        }
    }