package common

import com.google.gson.Gson
import jakarta.servlet.http.HttpServletRequest
import kotlin.runCatching

class HttpExtractor {
    // / Extract body content from the request
    inline fun <reified T : Any> extractBody(req: HttpServletRequest): Result<T, Throwable> {
        val gson = Gson()
        return runCatching {
            val body = req.reader.use { it.readText() }
            Ok(gson.fromJson(body.toString(), T::class.java))
        }.getOrElse { err -> Err(err) }
    }
}
