package application.models

import java.time.LocalDateTime

data class Event(
    val id: Int,
    val title: String,
    val description: String,
    val startedAt: LocalDateTime,
    val endedAt: LocalDateTime,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
