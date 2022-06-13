package app.server;

import app.controller.UserController
import app.database.DatabaseConnection
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder
import io.javalin.http.Context
import io.javalin.http.HttpCode
import org.jetbrains.exposed.sql.name
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*


object ServerSetup {
    private var app: Javalin = Javalin.create() { config ->
        config.enableDevLogging() // enable extensive development logging for http and websocket
    }.start(7000);

    private val log: Logger = LoggerFactory.getLogger(ServerSetup::class.java)

    fun init() {
        this.initDatabase();
        this.defineRoutes();
        this.defineExceptions();
    }

    private fun defineExceptions() {
        app.exception(
            MismatchedInputException::class.java
        ) { e, ctx ->
            ctx.status(HttpCode.BAD_REQUEST)
                .json(object {
                    val status = 400;
                    val message = e.message;
                    val stack = e.location;
                })
        };

        app.exception(
            Exception::class.java
        ) { e, ctx ->
            ctx.status(HttpCode.INTERNAL_SERVER_ERROR).json(object {
                val status = 500;
                val message = e.message;
                val stack = e.stackTraceToString();
            })
        }
    }

    private fun initDatabase() {
        try {
            DatabaseConnection.connect().let {
                log.info("Connected to Database ${it.name}")
            };
        } catch (e: java.lang.Exception) {
            log.error(e.toString());
        }
    }

    private fun defineRoutes() {
        app.routes {
            ApiBuilder.path("/") {
                ApiBuilder.get("") {
                    it.result("Hello World!!")
                }
            }
            UserController.defineEndpoints()
        }
    }
}