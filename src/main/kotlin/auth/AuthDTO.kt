package auth

data class LoginBody(val username: String, val password: String)

data class SignUpBody(val username: String, val password: String)
