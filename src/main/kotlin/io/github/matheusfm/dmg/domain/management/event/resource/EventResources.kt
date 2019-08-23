package io.github.matheusfm.dmg.domain.management.event.resource

import io.github.matheusfm.dmg.domain.management.event.EventType
import io.github.matheusfm.dmg.domain.management.event.service.EventService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import javax.validation.Valid

@RestController
class EventResources(val eventService: EventService) {

    @GetMapping("/cattleTypes")
    fun cattleTypesByEventType(@RequestParam eventType: EventType) = eventType.cattleTypes

    @GetMapping("/eventTypes")
    fun eventTypes() = EventType.values()

    @PostMapping("/events")
    @ResponseStatus(HttpStatus.CREATED)
    fun event(@RequestBody @Valid event: EventRequest) = mapOf("id" to eventService.saveEvent(event))

    @GetMapping("/balances")
    fun balanceByCattle(
        @RequestParam cattlemanId: String?,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate?
    ) = eventService.balanceByCattle(cattlemanId, date)
        .map { (type, balance) ->
            mapOf("cattle" to type, "balance" to balance)
        }.toList()
}