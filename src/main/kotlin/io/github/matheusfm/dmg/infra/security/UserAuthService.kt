package io.github.matheusfm.dmg.infra.security

import io.github.matheusfm.dmg.domain.user.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserAuthService(private val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String?) = (userRepository.findByUsername(username!!)
        ?: throw UsernameNotFoundException("User $username not found")).toUserDetails()

}