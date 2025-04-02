package common

sealed class Result<out T, out E> {
    inline fun <F> onError(transform: (E) -> F): Result<T, F> =
        when (this) {
            is Ok -> this
            is Err -> Err(transform(err))
        }

    inline fun getOrElse(errHandler: (E) -> Nothing): T =
        when (this) {
            is Ok -> value
            is Err -> errHandler(err)
        }
}

data class Ok<out T>(
    val value: T,
) : Result<T, Nothing>()

data class Err<out E>(
    val err: E,
) : Result<Nothing, E>()
