import annotations.EnvVar

data class AppConfig(
    @EnvVar("ROUND_HASHING")
    val roundHashing: Int = 0,
    @EnvVar("SECRET_KEY")
    val secretKey: String = "",
)
