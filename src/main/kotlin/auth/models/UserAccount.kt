package auth.models

import java.time.LocalDateTime

data class UserAccount(
    var id: String,
    var username: String,
    var hashedPassword: String,
    var isActive: Boolean,
    var createdAt: LocalDateTime,
    var updatedAt: LocalDateTime,
    var countMistake: Int,
)
