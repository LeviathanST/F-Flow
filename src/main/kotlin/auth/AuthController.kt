package auth

import common.HttpExtractor
import common.RouteProvider
import common.Router
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

class AuthController : RouteProvider {
    private val LOGIN_VIEW = "/view/auth/login.jsp"
    private val SIGNUP_VIEW = "/view/auth/signup.jsp"
    private val extractor = HttpExtractor()

    override fun build(router: Router) {
        router.register {
            get("auth/signup", ::signup_view)
            get("auth/login", ::login_view)
            post("auth/login", ::login)
        }
    }

    // View
    fun signup_view(req: HttpServletRequest, res: HttpServletResponse) {
        req.getRequestDispatcher(SIGNUP_VIEW).forward(req, res)
    }
    fun login_view(req: HttpServletRequest, res: HttpServletResponse) {
        req.getRequestDispatcher(LOGIN_VIEW).forward(req, res)
    }

    fun login(req: HttpServletRequest, res: HttpServletResponse) {
        val data: LoginBody = extractor.extractBody<LoginBody>(req)
    }
}
