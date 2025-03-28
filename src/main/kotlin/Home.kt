import auth.AuthController
import common.Kotlet
import common.Router
import jakarta.servlet.annotation.WebServlet

@WebServlet(urlPatterns = ["/app/*"])
class Home : Kotlet() {
    override fun build(router: Router) {
        router.addProvider(AuthController())
        router.register {
            get("home") { req, res -> req.getRequestDispatcher("/view/main.jsp").forward(req, res) }
        }
    }
}
