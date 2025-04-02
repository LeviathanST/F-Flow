package configs

import annotations.EnvVar

data class DatabaseConfig(
    @EnvVar(name = "DB_URL")
    var url: String = "",
    @EnvVar(name = "DB_DATABASE")
    var database: String = "",
    @EnvVar(name = "DB_USERNAME")
    var username: String = "",
    @EnvVar(name = "DB_PASSWORD")
    var password: String = "",
)
