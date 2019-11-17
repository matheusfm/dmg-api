package io.github.matheusfm.dmg.domain.user.service

import io.github.matheusfm.dmg.domain.user.User
import io.github.matheusfm.dmg.domain.user.repository.UserRepository
import io.github.matheusfm.dmg.domain.user.resource.UserRequest
import io.github.matheusfm.dmg.infra.security.JwtTokenProvider
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider,
    private val passwordEncoder: PasswordEncoder
) {

    fun login(username: String, password: String): String {
        val authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(username, password))
        return jwtTokenProvider.createToken(authentication)
    }

    fun findAll() = userRepository.findAll().map(User::toResponse)

    fun save(user: UserRequest) = userRepository.save(user.toUser(passwordEncoder::encode)).let(User::toResponse)

    fun findById(id: String) = userRepository.findByIdOrNull(id)?.let(User::toResponse)

    fun findByUsername(username: String) = userRepository.findByUsername(username)?.let(User::toResponse)

    fun delete(id: String) = userRepository.deleteById(id)

}