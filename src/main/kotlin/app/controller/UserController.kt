package app.controller;

import app.service.UserService
import app.table.user.User
import io.javalin.apibuilder.ApiBuilder.*;
import io.javalin.http.Context

object UserController {

    private fun getUser(ctx: Context) {
        ctx.json(UserService.getAllUser());
    }

    private fun addUser(ctx: Context) {
        val newUser = ctx.bodyAsClass(User::class.java);
        val createdUser = UserService.insertUser(newUser);
        ctx.json(createdUser);
    }

    fun defineEndpoints() {
        path("/user") {
            get("/", UserController::getUser)
            post("new", UserController::addUser)
        }
    }
}
