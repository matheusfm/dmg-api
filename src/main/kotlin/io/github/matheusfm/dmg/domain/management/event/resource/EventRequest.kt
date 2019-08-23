package io.github.matheusfm.dmg.domain.management.event.resource

import io.github.matheusfm.dmg.domain.management.event.CattleType
import io.github.matheusfm.dmg.domain.management.event.Event
import io.github.matheusfm.dmg.domain.management.event.EventType
import java.time.LocalDate
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class EventRequest(
    @field:NotBlank val cattlemanId: String?,
    @field:NotNull val eventType: EventType?,
    val date: LocalDate?,
    val fiscalDocumentType: String?,
    val fiscalDocumentSeries: String?,
    val fiscalDocumentNumber: String?,
    val supplierId: String?,
    @field:NotNull val cattle: Map<CattleType, Int>?
) {

    fun isCattleValid() = getCattleViolations().isEmpty()

    fun getCattleViolations() = cattle!!.keys.filterNot(eventType!!::accept)

    fun toEvent(cattleman: Event.Cattleman, supplier: Event.Supplier?) =
        Event(
            cattleman = cattleman,
            eventType = eventType!!,
            date = date,
            fiscalDocumentNumber = fiscalDocumentNumber,
            fiscalDocumentSeries = fiscalDocumentSeries,
            fiscalDocumentType = fiscalDocumentType,
            cattle = cattle!!.map { entry -> Event.Cattle(entry.key, entry.value) },
            supplier = supplier
        )
}