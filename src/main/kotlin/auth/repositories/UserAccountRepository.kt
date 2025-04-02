package auth.repositories

import UserNotFound
import auth.models.UserAccount
import common.Database
import common.Err
import common.Ok
import common.Result
import io.tekniq.jdbc.insert
import io.tekniq.jdbc.selectOne
import java.sql.SQLException

class UserAccountRepository {
    private val dataSource = Database().connect()

    fun create(
        username: String,
        password: String,
    ): Result<Unit, Throwable> =
        runCatching {
            val query = """
            INSERT INTO user_account (username, hashed_password)
            VALUES (?, ?)
            """
            val effected = dataSource.insert(query, username, password)
            if (effected <= 0) {
                throw SQLException("No rows effected")
            }
            Ok(Unit)
        }.getOrElse {
            Err(it)
        }

    // Find by username
    // @throws UserNotFound if username is not exists
    fun findByUsername(username: String): Result<UserAccount, Throwable> =
        runCatching {
            val query = """
            SELECT * FROM user_account
            WHERE username = ?
            """
            val account =
                dataSource.selectOne(query, username) {
                    UserAccount(
                        it.getString("id"),
                        it.getString("username"),
                        it.getString("hashed_password"),
                        it.getBoolean("is_active"),
                        it.getTimestamp("created_at").toLocalDateTime(),
                        it.getTimestamp("updated_at").toLocalDateTime(),
                        it.getInt("count_mistake"),
                    )
                } ?: throw UserNotFound(username)
            Ok(account)
        }.getOrElse {
            Err(it)
        }
}
