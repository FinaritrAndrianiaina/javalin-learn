package app.server;

import app.controller.UserController
import app.database.DatabaseConnection
import app.security.AuthManager
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder
import io.javalin.http.Context
import io.javalin.http.HttpCode
import io.javalin.plugin.openapi.OpenApiOptions
import io.javalin.plugin.openapi.OpenApiPlugin
import io.javalin.plugin.openapi.ui.ReDocOptions
import io.javalin.plugin.openapi.ui.SwaggerOptions
import io.swagger.v3.oas.models.info.Info
import org.jetbrains.exposed.sql.name
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*


object ServerSetup {
    private var app: Javalin = Javalin.create() { config ->
        config.enableDevLogging() // enable extensive development logging for http and websocket
        config.accessManager(AuthManager);
        config.registerPlugin(getOpenApiPlugin())
    }.start(7000);

    private fun getOpenApiPlugin() = OpenApiPlugin(
        OpenApiOptions(
            Info().apply {
                version("1.0")
                description("User API")
            }
        ).apply {
            path("/swagger-docs") // endpoint for OpenAPI json
            swagger(SwaggerOptions("/swagger-ui")) // endpoint for swagger-ui
            reDoc(ReDocOptions("/redoc")) // endpoint for redoc

        }
    )

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