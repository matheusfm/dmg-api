package io.github.matheusfm.dmg.domain.user.repository

import io.github.matheusfm.dmg.domain.user.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<User, String> {
    fun findByUsername(username: String) : User?
}