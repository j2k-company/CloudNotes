package site.j2k

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

object JWTFactory {
    private lateinit var secret: String
    private lateinit var issuer: String
    private lateinit var audience: String

    fun init(secret: String, issuer: String, audience: String) {
        this.secret = secret
        this.issuer = issuer
        this.audience = audience
    }

    fun getToken(userId: Int): String = JWT.create()
        .withIssuer(issuer)
        .withAudience(audience)
        .withClaim("user_id", userId)
        .withExpiresAt(Date(System.currentTimeMillis() + TOKEN_LIFETIME))
        .sign(Algorithm.HMAC256(secret))
}