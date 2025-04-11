package application.repositories

import application.dtos.GetEventDTO
import common.Database
import common.Err
import common.Ok
import common.Result
import io.tekniq.jdbc.delete
import io.tekniq.jdbc.insert
import io.tekniq.jdbc.select
import io.tekniq.jdbc.update
import java.sql.SQLException
import java.sql.Timestamp
import java.time.LocalDateTime

class EventRepository {
    private val dataSource = Database().connect()

    fun create(
        title: String,
        description: String,
        startedAt: LocalDateTime,
        endedAt: LocalDateTime,
    ): Result<Unit, Throwable> =
        runCatching {
            val query = """
               INSERT INTO event (title, description, started_at, ended_at)         
               VALUES (?, ?, ?, ?)
            """

            val rowsEffected =
                dataSource.insert(
                    query,
                    title,
                    description,
                    startedAt,
                    endedAt,
                )
            if (rowsEffected <= 0) {
                throw SQLException("Created event failed")
            }

            Ok(Unit)
        }.getOrElse { Err(it) }

    fun update(
        id: Int,
        title: String,
        description: String,
        startedAt: LocalDateTime,
        endedAt: LocalDateTime,
    ): Result<Unit, Throwable> =
        runCatching {
            val query = """
                UPDATE event
                SET 
                    title = ?,
                    description = ?,
                    started_at = ?,
                    ended_at = ?
                WHERE id = ?
            """
            val rowsEffected =
                dataSource.update(
                    query,
                    title,
                    description,
                    Timestamp.valueOf(startedAt),
                    Timestamp.valueOf(endedAt),
                    id,
                )

            if (rowsEffected <= 0) {
                throw SQLException("Update event failed!")
            }
            Ok(Unit)
        }.getOrElse { Err(it) }

    fun delete(id: Int): Result<Unit, Throwable> =
        runCatching {
            val query = """
                DELETE FROM event
                WHERE id = ?
            """

            val rowsEffected = dataSource.delete(query, id)
            if (rowsEffected <= 0) {
                throw SQLException("Delete event failed")
            }

            Ok(Unit)
        }.getOrElse { Err(it) }

    fun getAll(): Result<List<GetEventDTO>, Throwable> =
        runCatching {
            val query = """
                    SELECT id, title, description, started_at, ended_at, created_at FROM event 
                """
            val events =
                dataSource.select<GetEventDTO>(query) {
                    GetEventDTO(
                        it.getInt("id"),
                        it.getString("title"),
                        it.getString("description"),
                        it.getTimestamp("started_at").toLocalDateTime(),
                        it.getTimestamp("ended_at").toLocalDateTime(),
                        it.getTimestamp("created_at").toLocalDateTime(),
                    )
                }
            Ok(events)
        }.getOrElse { Err(it) }
}
