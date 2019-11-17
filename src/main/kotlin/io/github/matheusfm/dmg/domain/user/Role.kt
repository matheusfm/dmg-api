package io.github.matheusfm.dmg.domain.user

import org.springframework.security.core.GrantedAuthority

enum class Role : GrantedAuthority {
    ADMIN,
    USER;

    override fun getAuthority() = "ROLE_$name"

}