package io.github.matheusfm.dmg.domain.management.event.service

import io.github.matheusfm.dmg.domain.exception.CattlemanNotFoundException
import io.github.matheusfm.dmg.domain.management.cattleman.CattlemanRepository
import io.github.matheusfm.dmg.domain.management.event.CattleType
import io.github.matheusfm.dmg.domain.management.event.Event
import io.github.matheusfm.dmg.domain.management.event.resource.EventRequest
import io.github.matheusfm.dmg.domain.management.event.exception.InvalidCattleException
import io.github.matheusfm.dmg.domain.management.event.repository.EventRepository
import io.github.matheusfm.dmg.domain.management.supplier.SupplierRepository
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class EventService(
    private val eventRepository: EventRepository,
    private val cattlemanRepository: CattlemanRepository,
    private val supplierRepository: SupplierRepository
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    fun saveEvent(eventRequest: EventRequest): String {
        log.debug("Validating event request cattle")
        if (!eventRequest.isCattleValid()) {
            log.error("Event request cattle is invalid")
            throw InvalidCattleException(eventRequest.getCattleViolations(), eventRequest.eventType!!)
        }

        log.info("Finding cattleman by id {}", eventRequest.cattlemanId)
        val eventCattleman = (eventRequest.cattlemanId?.let(cattlemanRepository::findByIdOrNull)
            ?: throw CattlemanNotFoundException(eventRequest.cattlemanId!!)).let {
            Event.Cattleman(cattlemanId = it.id!!, name = it.name!!, document = it.document!!)
        }

        log.info("Finding supplier by id {}", eventRequest.supplierId)
        val eventSupplier =
            eventRequest.supplierId?.takeIf(String::isNotBlank)?.let(supplierRepository::findByIdOrNull)?.let {
                Event.Supplier(
                    supplierId = it.id!!,
                    name = it.name!!,
                    document = it.document!!,
                    state = it.state!!,
                    city = it.city!!,
                    stateRegistration = it.stateRegistration
                )
            }

        val event = eventRequest.toEvent(eventCattleman, eventSupplier)

        val savedEvent = eventRepository.save(event)
        savedEvent.autoEvent()?.let { autoEvent -> eventRepository.save(autoEvent) }
        return savedEvent.id ?: throw RuntimeException("Error saving event")
    }

    fun findByCattlemanAndDate(cattlemanId: String, dateFrom: LocalDate, dateTo: LocalDate) =
        eventRepository.findByCattlemanAndDate(cattlemanId, dateFrom, dateTo)

    fun balanceByCattle(cattlemanId: String?, localDate: LocalDate?): Map<CattleType, Int> {
        log.warn("Processing balance by cattle until {}", localDate)
        val balance = eventRepository.findByCattlemanBefore(cattlemanId, localDate)
            .asSequence()
            .flatMap { event ->
                event.cattle.groupBy { it.type }
                    .mapValues { it.value.sumBy { cattle -> cattle.quantity * event.eventType.multiplier } }
                    .asSequence()
            }
            .groupBy({ it.key }, { it.value })
            .mapValues { (_, values) -> values.sum() }

        log.debug("Setting zero as default value")
        return balance.asSequence().plus(CattleType.values().map { it to 0 }.toMap().asSequence())
            .distinct()
            .groupBy({ it.key }, { it.value })
            .mapValues { (_, values) -> values.first() }
    }
}