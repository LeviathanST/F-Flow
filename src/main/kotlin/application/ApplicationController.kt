package application

import common.kotlet.RouteProvider
import common.kotlet.Router
import common.Response
import common.Err
import common.HttpExtractor
import common.constants.GetEventCondition
import common.constants.Context
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import application.repositories.EventRepository
import application.dtos.CUEventDTO
import FFLowException
import auth.repositories.PermissionRepository
import auth.AuthService
import io.github.oshai.kotlinlogging.KotlinLogging

class ApplicationController : RouteProvider {
    private val EVENT_VIEW = "/view/app/event.jsp"

    private val authService = AuthService()
    private val appService = ApplicationService()
    private val logger = KotlinLogging.logger { }

    override fun build(router: Router) {
        router.register{
            get("events", ::event_view)

            post("events", ::create_event)
        }
    }

    fun event_view(req: HttpServletRequest, res: HttpServletResponse) {
        authService.checkPermissionWithContext(req, "view", Context.APP).onErrSend(res)
        val events = appService.getAllEvents().getOrSendErr(res) ?: return
        val ade = authService.checkPermissionWithContext(req, "event.cud", Context.APP).getOrNull()
        req.setAttribute("events", events)
        req.setAttribute("ade", ade ?: false)
        req.getRequestDispatcher(EVENT_VIEW).forward(req, res)
    }

    fun create_event(req: HttpServletRequest, res: HttpServletResponse){
        authService.checkPermissionWithContext(req, "event.cud", Context.APP).onErrSend(res)
        val event = HttpExtractor().extractBody<CUEventDTO>(req).getOrSendErr(res) ?: return
        appService.createEvent(event).onErrSend(res)
        Response(200, "Create event successful!", null).sendSuccess(res)
    }
}
