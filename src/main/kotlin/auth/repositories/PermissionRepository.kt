package auth.repositories
import common.Database
import common.Err
import common.Ok
import common.Result
import common.constants.Context
import io.github.oshai.kotlinlogging.KotlinLogging
import io.tekniq.jdbc.selectOne

class PermissionRepository {
    private val logger = KotlinLogging.logger {}
    private val ds = Database().connect()

    fun checkExistByAccountId(
        accountId: String,
        permissions: List<String>,
        context: Context,
    ): Result<Boolean, Throwable> =
        runCatching {
            val placeholder = permissions.joinToString(", ") { "\"$it\"" }
            val query = """
				SELECT 1
				FROM permission p
					JOIN role_permission rp ON rp.permission_id = p.id
					JOIN user_role ur ON ur.role_id = rp.role_id
				WHERE ur.account_id = ?
					AND p.context = ?
					AND p.name IN ($placeholder)
				"""

            val result =
                ds.selectOne(query, accountId, context.name.lowercase()) {
                    it.getBoolean(1)
                }

            Ok(result != null)
        }.getOrElse { Err(it) }
}
