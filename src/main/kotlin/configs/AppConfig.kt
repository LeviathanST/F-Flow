package configs
import annotations.EnvVar

data class AppConfig(
    @EnvVar("ROUND_HASHING")
    var roundHashing: Int = 0,
    @EnvVar("SECRET_KEY")
    var secretKey: String = "",
)
