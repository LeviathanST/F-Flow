package common.kotlet

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

typealias RouteHandler = (HttpServletRequest, HttpServletResponse) -> Unit

data class Routing(
    val method: String,
    val path: String,
    val handler: RouteHandler,
)

class Router {
    private val routes: MutableList<Routing> = mutableListOf()

    fun get(
        path: String,
        handler: RouteHandler,
    ) {
        routes.add(Routing("GET", path, handler))
    }

    fun post(
        path: String,
        handler: RouteHandler,
    ) {
        routes.add(Routing("POST", path, handler))
    }

    // Get all routes exist in application then compare with current request's path and method
    fun handle(
        req: HttpServletRequest,
        res: HttpServletResponse,
    ) {
        val path = req.pathInfo?.substring(1) ?: ""
        val method = req.method
        val route = routes.find { it.method == method && it.path == path }
        route?.handler?.invoke(req, res)
            ?: res.sendError(
                HttpServletResponse.SC_NOT_FOUND,
                "Route not found: $method - $path",
            )
    }

    // Register a group of routes
    fun addProvider(provider: RouteProvider) {
        provider.build(this)
    }

    fun register(block: Router.() -> Unit) {
        this.apply(block)
    }
}
