package io.github.matheusfm.dmg.domain.user

import io.github.matheusfm.dmg.domain.user.resource.UserResponse
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.userdetails.UserDetails

@Document(collection = "users")
data class User(
    @field:Id val id: String?,
    @field:Indexed(unique = true) val username: String,
    val password: String,
    val role: Role,
    val name: String
) {

    fun toUserDetails(): UserDetails = org.springframework.security.core.userdetails.User
        .withUsername(username)
        .password(password)
        .authorities(role)
        .accountExpired(false)
        .accountLocked(false)
        .credentialsExpired(false)
        .disabled(false)
        .build()

    fun toResponse() = UserResponse(id, username, role, name)
}