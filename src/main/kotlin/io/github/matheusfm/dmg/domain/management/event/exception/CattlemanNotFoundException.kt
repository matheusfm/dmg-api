package io.github.matheusfm.dmg.domain.management.event.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class CattlemanNotFoundException(val cattlemanId: String) :
    RuntimeException("Cattleman ID '$cattlemanId' not found")