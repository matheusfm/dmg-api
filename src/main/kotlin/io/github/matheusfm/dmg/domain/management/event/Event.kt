package io.github.matheusfm.dmg.domain.management.event

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate
import java.time.LocalDateTime

@Document(collection = "events", language = "portuguese")
data class Event(
    @field:Id val id: String? = null,
    val cattleman: Cattleman,
    val eventType: EventType,
    val date: LocalDate? = null,
    val fiscalDocumentType: String? = null,
    val fiscalDocumentSeries: String? = null,
    val fiscalDocumentNumber: String? = null,
    val supplier: Supplier? = null,
    val cattle: Collection<Cattle>,
    val creationDate: LocalDateTime = LocalDateTime.now(),
    val originalEventId: String? = null
) {

    fun cattleIn(): Int {
        if (eventType.multiplier == 1) {
            return cattle.sumBy(Cattle::quantity)
        }
        return 0
    }

    fun cattleOut(): Int {
        if (eventType.multiplier == -1) {
            return cattle.sumBy(Cattle::quantity)
        }
        return 0
    }

    fun autoEvent(): Event? {
        val autoCattle = cattle.mapNotNull { c -> eventType.autoEvent(c.type)?.let { Cattle(it, c.quantity) } }
        return eventType.autoEventType()
            ?.let { this.copy(id = null, cattle = autoCattle, eventType = it, originalEventId = id) }
    }

    data class Cattleman(val cattlemanId: String, val code: String?, val name: String, val document: String)

    data class Supplier(
        val supplierId: String,
        val code: String?,
        val name: String,
        val document: String,
        val stateRegistration: String?,
        val city: String,
        val state: String
    )

    data class Cattle(val type: CattleType, val quantity: Int)
}