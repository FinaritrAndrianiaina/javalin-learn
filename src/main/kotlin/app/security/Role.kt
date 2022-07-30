package app.security

import io.javalin.core.security.RouteRole

enum class Role : RouteRole {
    USER,
    ANONYMOUS
}