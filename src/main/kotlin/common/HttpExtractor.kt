package common

import com.google.gson.Gson
import jakarta.servlet.http.HttpServletRequest

class HttpExtractor {
    inline fun <reified T> extractBody(req: HttpServletRequest): T {
        val gson = Gson()
        val body = req.reader.use { it.readText() }
        return gson.fromJson(body.toString(), T::class.java)
    }
}
