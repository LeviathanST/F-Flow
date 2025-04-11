package auth.models

import common.constants.Context

data class Role(
    val id: Int,
    val name: String,
    val context: Context,
)
