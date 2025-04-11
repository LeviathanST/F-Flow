package application

import application.dtos.CUEventDTO
import application.dtos.GetEventDTO
import application.repositories.EventRepository
import common.Err
import common.Ok
import common.Result

class ApplicationService {
    private val eventRepository = EventRepository()

    fun getAllEvents(): Result<List<GetEventDTO>, Throwable> {
        val events = eventRepository.getAll().getOrElse { return Err(it) }
        return Ok(events)
    }

    fun createEvent(data: CUEventDTO): Result<Unit, Throwable> {
        data.validate().onError { return Err(it) }
        eventRepository.create(data.title, data.description, data.startedAt, data.endedAt).onError { return Err(it) }
        return Ok(Unit)
    }
}
