package common

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

typealias RouteHandler = (HttpServletRequest, HttpServletResponse) -> Unit

data class Routing(val method: String, val path: String, val handler: RouteHandler)

class Router {
    private val routes: MutableList<Routing> = mutableListOf()

    fun get(path: String, handler: RouteHandler) {
        routes.add(Routing("GET", path, handler))
    }
    fun post(path: String, handler: RouteHandler) {
        routes.add(Routing("POST", path, handler))
    }
    fun handle(req: HttpServletRequest, res: HttpServletResponse) {
        val path = req.pathInfo?.substring(1) ?: ""
        val method = req.method
        val route = routes.find { it.method == method && it.path == path }
        route?.handler?.invoke(req, res)
                ?: res.sendError(
                        HttpServletResponse.SC_NOT_FOUND,
                        "Route not found: $method - $path"
                )
    }
    fun addProvider(provider: RouteProvider) {
        provider.build(this)
    }
    fun register(block: Router.() -> Unit) {
        this.apply(block)
    }
}
