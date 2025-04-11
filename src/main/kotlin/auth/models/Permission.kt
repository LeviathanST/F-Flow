package auth.models

import common.constants.Context

data class Permission(
    val id: Int,
    val name: String,
    val context: Context,
)
