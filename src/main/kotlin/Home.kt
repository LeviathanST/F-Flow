import common.Kotlet
import common.Router
import jakarta.servlet.annotation.WebServlet

@WebServlet("/*")
class Home : Kotlet() {
    override fun build(router: Router) {
        router.register {
            get("home") { _, res -> res.getWriter().use { writer -> writer.println("HelloWorld") } }
        }
    }
}
