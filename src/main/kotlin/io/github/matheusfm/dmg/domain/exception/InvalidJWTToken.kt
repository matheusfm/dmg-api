package io.github.matheusfm.dmg.domain.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class InvalidJWTToken : RuntimeException("Expired or invalid JWT token")