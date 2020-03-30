package io.github.matheusfm.dmg.domain.user.resource

import io.github.matheusfm.dmg.domain.user.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.util.UriComponentsBuilder
import java.security.Principal
import javax.validation.Valid

@RestController
class UserResources(private val userService: UserService) {

    @PostMapping("/auths")
    @ResponseStatus(HttpStatus.CREATED)
    fun authentication(@RequestBody body: AuthenticationRequest): Map<String, String?> {
        val token = userService.login(body.username, body.password)
        val user = userService.findByUsername(body.username)
        return mapOf("token" to token, "name" to user?.name)
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    fun getUsers() = userService.findAll()

    @GetMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun getUser(@PathVariable id: String) = userService.findById(id)
        ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    @GetMapping("/me")
    fun getMe(principal: Principal) = userService.findByUsername(principal.name)
        ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    @PostMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    fun postUsers(@RequestBody @Valid body: UserRequest, uriBuilder: UriComponentsBuilder): ResponseEntity<UserResponse> {
        val user = userService.save(body)
        return ResponseEntity.created(uriBuilder.path("/users/{id}").buildAndExpand(user.id).toUri()).body(user)
    }

}