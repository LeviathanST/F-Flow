package application.dtos

import FFLowException
import InvalidData
import common.Err
import common.Ok
import common.Result
import java.time.LocalDateTime

data class GetEventDTO(
    val id: Int,
    val title: String,
    val description: String,
    val startedAt: LocalDateTime,
    val endedAt: LocalDateTime,
    val createdAt: LocalDateTime,
) {
    fun validate(): Result<Unit, FFLowException> =
        runCatching {
            require(startedAt.isBefore(LocalDateTime.now())) {
                "Started time cannot in the pass!"
            }
            require(endedAt.isBefore(startedAt)) {
                "Ended time cannot before started time!"
            }
            return Ok(Unit)
        }.getOrElse {
            return Err(InvalidData(it.message!!))
        }
}

/*
 * Using same dto because same field visible
 * Create and update event dto
 * */
data class CUEventDTO(
    val id: Int?,
    val title: String,
    val description: String,
    var startedAt: LocalDateTime,
    var endedAt: LocalDateTime,
) {
    fun validate(): Result<Unit, FFLowException> =
        runCatching {
            require(!startedAt.isBefore(LocalDateTime.now())) {
                "Started time cannot in the pass!"
            }
            require(startedAt.isBefore(endedAt)) {
                "Ended time cannot before started time!"
            }
            return Ok(Unit)
        }.getOrElse {
            return Err(InvalidData(it.message!!))
        }
}

data class DeleteEventDTO(
    val id: Int,
)
