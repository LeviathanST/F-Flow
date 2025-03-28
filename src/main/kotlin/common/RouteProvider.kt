package common

interface RouteProvider {
    // Route register
    fun build(router: Router)
}
