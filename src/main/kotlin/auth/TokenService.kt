package auth
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import common.EnvLoader
import configs.AppConfig
import io.github.oshai.kotlinlogging.KotlinLogging

data class TokenPair(
    val accessToken: String,
    // TODO: refresh token
)

class TokenService {
    private val logger = KotlinLogging.logger {}
    private val secret: String by lazy { EnvLoader.load(AppConfig::class).getOrElse { throw it }.secretKey }

    fun generate(
        account_id: String,
        role_id: Int,
    ): String {
        val algorithms = Algorithm.HMAC256(secret)
        val token =
            JWT
                .create()
                .withIssuer("auth0")
                .withClaim("account_id", account_id)
                .withClaim("user_role_id", role_id)
                .sign(algorithms)
        return token
    }
}
