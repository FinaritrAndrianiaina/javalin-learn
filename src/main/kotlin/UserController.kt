import io.javalin.apibuilder.ApiBuilder.*;
import io.javalin.http.Context

data class User(val name: String, val age: Int) {


}

object UserController {
    private val users = arrayListOf(
        User("rakoto", 35),
        User("francis", 15)
    );

    private fun getUser(ctx: Context) {
        ctx.json(users);
    }

    private fun addUser(ctx: Context) {
        val newUser = ctx.bodyAsClass(User::class.java);
        users.add(newUser);
        ctx.json(users.last());
    }

    fun defineEndpoints() {
        path("/user") {
            get("/", UserController::getUser)
            post("new", UserController::addUser)
        }
    }
}
