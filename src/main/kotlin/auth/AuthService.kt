package auth

import auth.repositories.UserAccountRepository
import common.Err
import common.Ok
import common.Result
import common.EnvLoader
import common.constants.Context
import configs.AppConfig
import at.favre.lib.crypto.bcrypt.BCrypt
import java.sql.SQLIntegrityConstraintViolationException
import InvalidCredentials
import auth.repositories.RoleRepository
import auth.repositories.PermissionRepository
import DataExisted
import jakarta.servlet.http.HttpServletRequest
import FFLowException
import io.github.oshai.kotlinlogging.KotlinLogging
import Unauthorized

class AuthService {
    private val userAccountRepository = UserAccountRepository()
    private val roleRepository = RoleRepository()
    private val permissionRepository = PermissionRepository()
    private val tokenService = TokenService()

    private val logger = KotlinLogging.logger{}

    fun login(data: LoginBody): Result<String, Throwable> {
        data.validate().onError {
            return@login Err(it)
        }
        val account =
            userAccountRepository.findByUsername(data.username).getOrElse {
                return@login Err(it)
            }
        val roleId = roleRepository
                    .findIdByUsername(data.username)
                    .getOrElse { 
                        return@login Err(it)
                    }
        val hash_result = BCrypt.verifyer().verify(data.password.toCharArray(), account.hashedPassword)
        if (!hash_result.verified) {
            return Err(InvalidCredentials("Wrong password"))
        }
        val token = tokenService.generate(account.id, roleId)
        return Ok(token)
    }

    fun signup(data: SignUpBody): Result<Unit, Throwable> {
        data.validate().onError {
            return@signup Err(it)
        }
        val roundHashing: Int = 
            EnvLoader
            .load(AppConfig::class)
            .getOrElse { return@signup Err(it) }
            .roundHashing

        val hash = BCrypt.withDefaults().hashToString(roundHashing, data.password.toCharArray())
        userAccountRepository.create(data.username, hash).onError { 
            return@signup when (it){
                is SQLIntegrityConstraintViolationException -> Err(DataExisted("User is existed!"))
                else -> Err(it)
            }
        }

        return Ok(Unit)
    }
    fun checkPermissionWithContext(req: HttpServletRequest, permission: String, context: Context): Result<Boolean, Throwable>{
        val accountId = getAccountIdFromCookieRequest(req).getOrElse{ return Err(it)}
        val status = permissionRepository.checkExistByAccountId(accountId, listOf(permission, "*"), context).getOrElse { return Err(it)}
        if (!status) {
            return Err(Unauthorized())
        }
        return Ok(true)
    }
    fun checkPermissionWithoutThrow(req: HttpServletRequest, permission: String, context: Context): Result<Boolean, Throwable>{
        val accountId = getAccountIdFromCookieRequest(req).getOrElse{ return Err(it)}
        val status = permissionRepository.checkExistByAccountId(accountId, listOf(permission, "*"), context)
        return status
    }
    private fun getAccountIdFromCookieRequest(req: HttpServletRequest): Result<String, FFLowException>{
        val at = req.getCookies().find{ it.name == "at" }?.getValue()
        if (at == null){
            return Err(InvalidCredentials("Not found your token!"))
        }
        val accountId = tokenService.verify(at)
        return Ok(accountId)
    }
}
