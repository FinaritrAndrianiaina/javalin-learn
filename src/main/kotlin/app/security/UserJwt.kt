package app.security

import app.table.user.User
import io.github.nefilim.kjwt.*
import java.time.LocalDateTime
import java.util.*

class UserJwt(user: User) {
    val jwt: JWT<JWSHMAC256Algorithm>;
    private val SECRET = "SECRET_PASSWORD"

    init {
        jwt = JWT.hs256(JWTKeyID("USER-${user.id}")) {
            subject(user.id.toString())
            issuer("localhost@javalin")
            claim("name", user.name)
            expiresAt(LocalDateTime.now().plusDays(2))
            issuedNow()
        }
    }

    val encoded = Optional.ofNullable(jwt.sign(SECRET).orNull()).orElseThrow {
        throw Exception("Unauthorized User");
    }.rendered

    companion object {

        fun decode(token: String): DecodedJWT<JWSHMAC256Algorithm> {
            return Optional.ofNullable(JWT.decodeT(token, JWSHMAC256Algorithm).orNull()).orElseThrow() {
                throw Exception("Unauthorized User");
            }
        }
    }
}
