package app.security

import app.security.extensions.userRoles
import io.javalin.core.security.AccessManager
import io.javalin.core.security.RouteRole
import io.javalin.http.Context
import io.javalin.http.Handler
import io.javalin.http.HttpCode

object AuthManager : AccessManager {

    override fun manage(handler: Handler, ctx: Context, rolesAllowed: MutableSet<RouteRole>) {
        println(rolesAllowed)
        when {
            rolesAllowed.isEmpty() or rolesAllowed.contains(Role.ANONYMOUS) -> handler.handle(ctx)
            ctx.userRoles.any { it in rolesAllowed } -> handler.handle(ctx)
            else -> ctx.status(HttpCode.UNAUTHORIZED).json("Unauthorized")
        }
    }

}