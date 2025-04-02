import auth.AuthController
import common.kotlet.Kotlet
import common.kotlet.Router
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.annotation.WebServlet

@WebServlet(urlPatterns = ["/app/*"])
class Home : Kotlet() {
    private val logger = KotlinLogging.logger {}

    override fun build(router: Router) {
        router.addProvider(AuthController())
        router.register {
            get("home") { req, res -> req.getRequestDispatcher("/view/main.jsp").forward(req, res) }
        }
    }
}
