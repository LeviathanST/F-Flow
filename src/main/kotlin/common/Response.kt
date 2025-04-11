package common
import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletResponse

data class Response<out T>(
    @Expose val statusCode: Int,
    @Expose var message: String,
    @Expose val data: T?,
) {
    private val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()

    /* Send err to the client then logging into server
     * */
    fun sendErr(
        res: HttpServletResponse,
        e: Throwable,
    ) {
        val logger = KotlinLogging.logger {}
        logger.error { e.stackTraceToString() }
        res.status = statusCode
        res.getWriter().use { writer -> writer.println(gson.toJson(this)) }
    }

    /*  Send attachted data to the client
     * */
    fun sendSuccess(res: HttpServletResponse) {
        res.status = statusCode
        res.getWriter().use { writer -> writer.println(gson.toJson(this)) }
    }
}
