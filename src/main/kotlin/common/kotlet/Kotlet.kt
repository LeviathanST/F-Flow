package common.kotlet

import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

abstract class Kotlet : HttpServlet() {
    private val router = Router()

    // The function use to register routes for the application
    abstract fun build(router: Router)

    // The entry when tomcat is started
    override fun init() {
        build(router)
    }

    // Custom how tomcat treats route in the application
    override fun service(
        req: HttpServletRequest,
        res: HttpServletResponse,
    ) {
        router.handle(req, res)
    }
}
