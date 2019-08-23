package io.github.matheusfm.dmg.domain.report.service

import io.github.matheusfm.dmg.domain.exception.CattlemanNotFoundException
import io.github.matheusfm.dmg.domain.management.cattleman.CattlemanRepository
import io.github.matheusfm.dmg.domain.management.event.Event
import io.github.matheusfm.dmg.domain.management.event.service.EventService
import io.github.matheusfm.dmg.domain.report.generator.ReportData
import io.github.matheusfm.dmg.domain.report.generator.ReportData.FiscalDocument
import io.github.matheusfm.dmg.domain.report.generator.ReportData.Taxpayer
import io.github.matheusfm.dmg.domain.report.resource.ReportResponse
import io.github.matheusfm.dmg.domain.report.generator.ReportGenerator
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ReportService(
    val cattlemanRepository: CattlemanRepository,
    val eventService: EventService,
    val reportGenerator: ReportGenerator
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    fun report(cattlemanId: String, dateFrom: LocalDate, dateTo: LocalDate): ReportResponse {

        log.info("Finding Cattleman by ID {}", cattlemanId)
        val cattleman = cattlemanRepository.findByIdOrNull(cattlemanId)
            ?: throw CattlemanNotFoundException(cattlemanId)

        log.info("Finding Events by Cattleman ID {} between {} and {}", cattlemanId, dateFrom, dateTo)
        val events = eventService.findByCattlemanAndDate(cattlemanId, dateFrom, dateTo)

        val cattleBalance = eventService.balanceByCattle(cattlemanId, dateFrom)

        log.debug("Grouping cattle quantity by event types")
        val cattleEvents = events
            .asSequence()
            .groupBy(Event::eventType)
            .mapValues { (_, events) ->
                events.flatMap(Event::cattle)
                    .groupBy(Event.Cattle::type)
                    .mapValues { (_, cattleEvents) -> cattleEvents.sumBy(Event.Cattle::quantity) }
            }

        log.debug("Building fiscal documents from events")
        val fiscalDocuments = events.asSequence()
            .filterNot { event -> event.fiscalDocumentNumber.isNullOrBlank() }
            .map(::FiscalDocument).toList()

        log.debug("Building report data")
        val reportData = ReportData(
            dateFrom,
            dateTo,
            Taxpayer(cattleman),
            cattleEvents,
            cattleBalance,
            fiscalDocuments
        )

        log.debug("Generating report")
        val file = reportGenerator.generateReport(reportData)

        return ReportResponse(reportData.name(), file)
    }

}