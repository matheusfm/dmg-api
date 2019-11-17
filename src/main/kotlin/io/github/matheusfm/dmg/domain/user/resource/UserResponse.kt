package io.github.matheusfm.dmg.domain.user.resource

import io.github.matheusfm.dmg.domain.user.Role

data class UserResponse(
    val id: String?,
    val username: String,
    val role: Role,
    val name: String
)