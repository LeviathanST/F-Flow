package common

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import jakarta.servlet.http.HttpServletRequest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.runCatching

class HttpExtractor {
    // Extract body content from the request
    inline fun <reified T : Any> extractBody(req: HttpServletRequest): Result<T, Throwable> {
        val dateFormat = "yyyy-MM-dd HH:mm:ss"
        val formatter = DateTimeFormatter.ofPattern(dateFormat)
        val gson =
            GsonBuilder()
                .registerTypeAdapter(
                    LocalDateTime::class.java,
                    JsonDeserializer { json, _, _ ->
                        LocalDateTime.parse(json.asJsonPrimitive.asString)
                    } as JsonDeserializer<LocalDateTime?>,
                ).registerTypeAdapter(
                    LocalDateTime::class.java,
                    JsonSerializer<LocalDateTime?> { localDate, _, _ ->
                        JsonPrimitive(formatter.format(localDate))
                    },
                ).create()
        return runCatching {
            val body = req.reader.use { it.readText() }
            Ok(gson.fromJson<T>(body.toString(), T::class.java))
        }.getOrElse { err -> Err(err) }
    }
}
