package io.github.matheusfm.dmg.domain.management.event.exception

import io.github.matheusfm.dmg.domain.management.event.CattleType
import io.github.matheusfm.dmg.domain.management.event.EventType
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class InvalidCattleException(val violations: List<CattleType>, val eventType: EventType) :
    RuntimeException("$violations cattle are invalid to the event type $eventType")