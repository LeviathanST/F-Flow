package role

import DataNotFound
import common.Database
import common.Err
import common.Ok
import common.Result
import io.tekniq.jdbc.selectOne

class RoleRepository {
    private val dataSource = Database().connect()

    fun findIdByUsername(username: String): Result<Int, Throwable> =
        runCatching {
            val query = """
                SELECT r.id FROM role r
                JOIN user_role ur ON ur.role_id = r.id
                JOIN user_account ua ON ua.id = ur.account_id
                WHERE ua.username = ?
            """
            val id =
                dataSource.selectOne(query, username) {
                    it.getInt("id")
                }

            if (id == null) {
                throw DataNotFound("Role in the application", username)
            }

            Ok(id)
        }.getOrElse { Err(it) }
}
