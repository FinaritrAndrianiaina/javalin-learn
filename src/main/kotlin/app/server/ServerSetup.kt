package app.server;

import UserController
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.usthe.sureness.DefaultSurenessConfig
import com.usthe.sureness.mgt.SurenessSecurityManager
import com.usthe.sureness.processor.exception.*
import com.usthe.sureness.util.JsonWebTokenUtil
import com.usthe.sureness.util.SurenessContextHolder
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder
import io.javalin.http.Context
import io.javalin.http.HttpCode
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*


object ServerSetup {
    private var app: Javalin = Javalin.create() { config ->
        config.enableDevLogging() // enable extensive development logging for http and websocket
    }.start(7000);

    private val log: Logger = LoggerFactory.getLogger(ServerSetup::class.java)

    fun init() {
        DefaultSurenessConfig();
        app.before {
            val securityManager = SurenessSecurityManager.getInstance();
            val subjectSum = securityManager.checkIn(it.req);
            if (subjectSum != null) {
                SurenessContextHolder.bindSubject(subjectSum);
            }
        }

        app.after { SurenessContextHolder.unbindSubject() }

        this.configAuth();
        this.defineRoutes();
        this.filterAuthExceptions();

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


    }

    private fun filterAuthExceptions() {
        app.exception(
            UnknownAccountException::class.java
        ) { e: UnknownAccountException, ctx: Context ->
            log.debug("this request user account not exist")
            ctx.status(401).result(e.message!!)
        }.exception(
            IncorrectCredentialsException::class.java
        ) { e: IncorrectCredentialsException, ctx: Context ->
            log.debug("this account credential is incorrect")
            ctx.status(401).result(e.message!!)
        }.exception(
            ExpiredCredentialsException::class.java
        ) { e: ExpiredCredentialsException, ctx: Context ->
            log.debug("this account credential expired")
            ctx.status(401).result(e.message!!)
        }.exception(
            NeedDigestInfoException::class.java
        ) { e: NeedDigestInfoException, ctx: Context ->
            log.debug("you should try once again with digest auth information")
            ctx.status(401).header("WWW-Authenticate", e.authenticate)
        }.exception(
            UnauthorizedException::class.java
        ) { e: UnauthorizedException, ctx: Context ->
            log.debug("this account can not access this resource")
            ctx.status(403).result(e.message!!)
        }.exception(Exception::class.java) { e: Exception, ctx: Context ->
            log.error("other exception happen: ", e)
            ctx.status(500).result(e.message!!)
        }
    };

    private fun configAuth() {

        app["/auth/token", { ctx: Context ->
            val subjectSum = SurenessContextHolder.getBindSubject()
            if (subjectSum == null) {
                ctx.result("Please auth!")
            } else {
                val principal = subjectSum.principal as String
                val roles =
                    subjectSum.roles as List<String>
                // issue jwt
                val jwt = JsonWebTokenUtil.issueJwt(
                    UUID.randomUUID().toString(), principal,
                    "token-server", 3600L, roles
                )
                ctx.result(jwt)
            }
        }]
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