package auth

import common.Response
import common.HttpExtractor
import common.kotlet.RouteProvider
import common.kotlet.Router
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.Cookie
import FFLowException

class AuthController : RouteProvider {
    private val LOGIN_VIEW = "/view/auth/login.jsp"
    private val SIGNUP_VIEW = "/view/auth/signup.jsp"

    private val extractor = HttpExtractor()
    private val logger = KotlinLogging.logger {}
    private val authService = AuthService()

    override fun build(router: Router) {
        router.register {
            get("auth/signup", ::signup_view)
            get("auth/login", ::login_view)

            post("auth/login", ::login)
            post("auth/signup", ::signup)
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
        val data =
                extractor
                .extractBody<LoginBody>(req)
                .getOrSendErr(res) ?: return

        val token = authService.login(data).getOrElse {
            when (it) {
                is FFLowException -> Response(
                    400,
                    it.message ?: "Login failed!",
                    null)
                    .sendErr(res, it)

                else -> Response(
                    500,
                    "Internal Server Error: Please contact with our!",
                    null)
                    .sendErr(res, it)
            }
            return@login
        }
        val cookieHeader = "at=$token; Path=/; HttpOnly; SameSite=Strict"
        res.setHeader("Set-Cookie", cookieHeader)

        Response(200, "Login successfuly!", null).sendSuccess(res)
    }
    fun signup(req: HttpServletRequest, res: HttpServletResponse) {
        val data =
                extractor.extractBody<SignUpBody>(req).getOrElse {
                    Response(500, "Internal Server Error: Please contact with our!", null).sendErr(res, it)
                    return@signup
                }

        authService.signup(data).onError {
            when (it) {
                is FFLowException-> Response(
                    400,
                    it.message ?: "Signup failed!",
                    null)
                    .sendErr(res, it)

                else -> Response(
                    500,
                    "Internal Server Error: Please contact with our!",
                    null)
                    .sendErr(res, it)
            }
            return@signup
        }
        Response(200, "Signup successfuly!", null).sendSuccess(res)
    }
}
