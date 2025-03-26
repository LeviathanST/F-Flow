import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.io.PrintWriter

@WebServlet("/*")
class Home : HttpServlet() {
    override fun doGet(req: HttpServletRequest, res: HttpServletResponse) {
        res.contentType = "text/html"
        val writer: PrintWriter = res.getWriter()
        writer.use { w -> w.println("HelloWorld") }
    }
}
