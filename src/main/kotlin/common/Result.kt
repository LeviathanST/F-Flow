package common

import FFLowException
import Unauthorized
import jakarta.servlet.http.HttpServletResponse

sealed class Result<out T, out E> {
    inline fun getOrElse(errHandler: (E) -> Nothing): T =
        when (this) {
            is Ok -> value
            is Err -> errHandler(err)
        }

    fun getOrSendErr(res: HttpServletResponse): T? =
        when (this) {
            is Ok -> value
            is Err -> {
                onErrSend(res)
                null
            }
        }

    fun getOrNull(): T? =
        when (this) {
            is Ok -> value
            is Err -> null
        }

    inline fun <F> onError(transform: (E) -> F): Result<T, F> =
        when (this) {
            is Ok -> this
            is Err -> Err(transform(err))
        }

    fun onErrSend(res: HttpServletResponse) {
        when (this) {
            is Ok -> Unit
            is Err -> {
                when (err) {
                    is Unauthorized -> Response(401, err.message, null).sendErr(res, err as Throwable)
                    is FFLowException -> Response(400, err.message!!, null).sendErr(res, err as Throwable)
                    else -> Response(500, "Internal Server Error: Please contact with our!", null).sendErr(res, err as Throwable)
                }
            }
        }
    }
}

data class Ok<out T>(
    val value: T,
) : Result<T, Nothing>()

data class Err<out E>(
    val err: E,
) : Result<Nothing, E>()
