package io.github.matheusfm.dmg.domain.user.resource

import io.github.matheusfm.dmg.domain.user.Role
import io.github.matheusfm.dmg.domain.user.User
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class UserRequest(
    @field:NotBlank val username: String?,
    @field:NotNull val role: Role?,
    @field:NotBlank val name: String?,
    @field:NotBlank val password: String?
) {

    fun toUser(passwordEncoder: (String) -> String) =
        User(id = null, username = username!!, role = role!!, name = name!!, password = passwordEncoder(password!!))
}