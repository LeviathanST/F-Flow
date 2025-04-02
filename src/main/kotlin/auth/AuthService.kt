package auth

import auth.repositories.UserAccountRepository
import common.Err
import common.Ok
import common.Result
import at.favre.lib.crypto.bcrypt.BCrypt
import java.sql.SQLIntegrityConstraintViolationException
import InvalidCredentials
import role.RoleRepository
import DataExisted

class AuthService {
    private val userAccountRepository = UserAccountRepository()
    private val roleRepository = RoleRepository()
    private val tokenService = TokenService()

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
        val roundHashing: Int = 4
        val hash = BCrypt.withDefaults().hashToString(roundHashing, data.password.toCharArray())
        userAccountRepository.create(data.username, hash).onError { 
            return@signup when (it){
                is SQLIntegrityConstraintViolationException -> Err(DataExisted("User is existed!"))
                else -> Err(it)
            }
        }

        return Ok(Unit)
    }
}
