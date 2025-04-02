package auth

import InvalidCredentials
import common.Err
import common.Ok
import common.Result
import org.jetbrains.annotations.NotNull
import kotlin.runCatching

class LoginBody(
    @NotNull val username: String,
    @NotNull val password: String,
) {
    fun validate(): Result<LoginBody, Throwable> =
        runCatching {
            require(username.trim().isNotBlank()) {
                "Username cannot be empty or just contains whitespace"
            }
            require(password.trim().isNotBlank()) {
                "Password cannot be empty or just contains whitespace"
            }
            Ok(this)
        }.getOrElse { err -> Err(InvalidCredentials(err.message ?: "Failed in data validation!")) }
}

data class SignUpBody(
    @NotNull val username: String,
    @NotNull val password: String,
) {
    fun validate(): Result<SignUpBody, Throwable> =
        runCatching {
            require(username.trim().isNotBlank()) {
                "Username cannot be empty or just contains whitespace"
            }
            require(password.trim().isNotBlank()) {
                "Password cannot be empty or just contains whitespace"
            }
            Ok(this)
        }.getOrElse { err -> Err(InvalidCredentials(err.message ?: "Failed in data validation!")) }
}
