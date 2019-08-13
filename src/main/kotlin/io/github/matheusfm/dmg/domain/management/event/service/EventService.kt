package io.github.matheusfm.dmg.domain.management.event.service

import io.github.matheusfm.dmg.domain.management.cattleman.CattlemanRepository
import io.github.matheusfm.dmg.domain.management.event.Event
import io.github.matheusfm.dmg.domain.management.event.EventRequest
import io.github.matheusfm.dmg.domain.management.event.exception.CattlemanNotFoundException
import io.github.matheusfm.dmg.domain.management.event.exception.InvalidCattleException
import io.github.matheusfm.dmg.domain.management.event.repository.EventRepository
import io.github.matheusfm.dmg.domain.management.supplier.SupplierRepository
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class EventService(
    private val eventRepository: EventRepository,
    private val cattlemanRepository: CattlemanRepository,
    private val supplierRepository: SupplierRepository
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    fun saveEvent(eventRequest: EventRequest): String {
        log.info("Validating event request cattle")
        if (!eventRequest.isCattleValid()) {
            log.error("Event request cattle is invalid")
            throw InvalidCattleException(eventRequest.getCattleViolations(), eventRequest.eventType!!)
        }

        log.info("Finding cattleman by id {}", eventRequest.cattlemanId)
        val eventCattleman = (eventRequest.cattlemanId?.let(cattlemanRepository::findByIdOrNull)
            ?: throw CattlemanNotFoundException(eventRequest.cattlemanId!!)).let {
            Event.Cattleman(
                cattlemanId = it.id!!,
                name = it.name!!,
                document = it.document!!
            )
        }

        log.info("Finding supplier by id {}", eventRequest.supplierId)
        val eventSupplier =
            eventRequest.supplierId?.takeIf(String::isNotBlank)?.let(supplierRepository::findByIdOrNull)?.let {
                Event.Supplier(
                    supplierId = it.id!!,
                    name = it.name!!,
                    document = it.document!!
                )
            }

        val event = eventRequest.toEvent(eventCattleman, eventSupplier)
        return eventRepository.save(event).id ?: throw RuntimeException("Error saving event")
    }
}