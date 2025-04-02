package common.kotlet

interface RouteProvider {
    // Route register
    fun build(router: Router)
}
