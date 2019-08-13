package io.github.matheusfm.dmg.domain.management.event

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate
import java.time.LocalDateTime

@Document(collection = "events")
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
    val creationDate: LocalDateTime = LocalDateTime.now()
) {

    data class Cattleman(val cattlemanId: String, val name: String, val document: String)

    data class Supplier(val supplierId: String, val name: String, val document: String)

    data class Cattle(val type: CattleType, val quantity: Int)
}