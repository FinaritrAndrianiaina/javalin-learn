package app.controller;

import app.security.Role
import app.service.UserService
import app.table.user.User
import io.javalin.apibuilder.ApiBuilder.*;
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.OpenApi
import io.javalin.plugin.openapi.annotations.OpenApiContent
import io.javalin.plugin.openapi.annotations.OpenApiResponse

object UserController {

    @OpenApi(
        summary = "Get all users",
        operationId = "getAllUsers",
        tags = ["User"],
        responses = [OpenApiResponse("200", [OpenApiContent(Array<User>::class)])]
    )
    private fun getUser(ctx: Context) {
        ctx.json(UserService.getAllUser());
    }

    private fun addUser(ctx: Context) {
        val newUser = ctx.bodyAsClass(User::class.java);
        val createdUser = UserService.insertUser(newUser);
        ctx.json(createdUser);
    }

    private fun getTokenId(ctx: Context) {
        val id = ctx.pathParam("id")
        ctx.result(UserService.generateToken(id.toInt()));
    }

    fun defineEndpoints() {
        path("/user") {
            get("/", UserController::getUser, Role.ANONYMOUS)
            post("new", UserController::addUser, Role.ANONYMOUS)
            get("/token/{id}", UserController::getTokenId, Role.USER)
        }
    }

}
