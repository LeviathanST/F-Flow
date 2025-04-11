sealed class FFLowException(
    message: String,
) : Throwable()

data class UserNotFound(
    val username: String,
    override val message: String = "User with username $username not found!",
) : FFLowException(message)

data class DataNotFound(
    val dataName: String,
    val username: String,
    override val message: String = "$dataName with username $username not found!",
) : FFLowException(message)

/*
 * Occur when user send the invalid credentials to server
 * */
data class InvalidCredentials(
    override val message: String,
) : FFLowException(message)

data class Unauthorized(
    override val message: String = "User don't have permission",
) : FFLowException(message)

data class InvalidData(
    override val message: String,
) : FFLowException(message)

data class DataExisted(
    override val message: String,
) : FFLowException(message)
