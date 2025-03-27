package common

import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

abstract class Kotlet : HttpServlet() {
    private val router = Router()
    // Entry function
    abstract fun build(router: Router)

    override fun init() {
        build(router)
    }
    override fun service(req: HttpServletRequest, res: HttpServletResponse) {
        router.handle(req, res)
    }
}
