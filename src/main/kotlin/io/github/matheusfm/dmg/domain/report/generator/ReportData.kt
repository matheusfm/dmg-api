package io.github.matheusfm.dmg.domain.report.generator

import io.github.matheusfm.dmg.domain.management.cattleman.Cattleman
import io.github.matheusfm.dmg.domain.management.event.CattleType
import io.github.matheusfm.dmg.domain.management.event.Event
import io.github.matheusfm.dmg.domain.management.event.EventType
import io.github.matheusfm.dmg.normalize
import io.github.matheusfm.dmg.reportFormat
import io.github.matheusfm.dmg.reportFormatUntil
import java.time.LocalDate

data class ReportData(
    val period: String,
    val year: Int,
    val issuer: Taxpayer,
    val owner: Taxpayer,
    val dateFrom: String,
    val cattleEvents: Map<String, Map<String, Int>>,
    val cattleBalance: Map<String, CattleBalance>,
    val fiscalDocuments: Collection<FiscalDocument>
) {

    constructor(
        dateFrom: LocalDate,
        dateTo: LocalDate,
        cattleman: Taxpayer,
        cattleEvents: Map<EventType, Map<CattleType, Int>>,
        cattleBalance: Map<CattleType, Int>,
        fiscalDocuments: Collection<FiscalDocument>
    ) : this(
        period = dateFrom.reportFormatUntil(dateTo),
        year = dateFrom.year,
        issuer = cattleman,
        owner = cattleman,
        dateFrom = dateFrom.reportFormat(),
        cattleEvents = cattleEvents
            .mapValues { (_, entry) -> entry.mapKeys { (cattle, _) -> cattle.name } }
            .mapKeys { (eventType, _) -> eventType.name },
        cattleBalance = cattleBalance
            .mapValues { (cattle, balance) ->
                CattleBalance(
                    balance,
                    cattle
                )
            }
            .mapKeys { (cattle, _) -> cattle.name },
        fiscalDocuments = fiscalDocuments
    )

    fun name() = "DMG_${issuer.name}.xls".normalize().replace("\\s", "")

    data class Taxpayer(
        val name: String?,
        val address: String?,
        val city: String?,
        val state: String?,
        val document: String?,
        val stateRegistration: String?,
        val cnae: String?
    ) {
        constructor(cattleman: Cattleman) : this(
            cattleman.name,
            cattleman.address,
            cattleman.city,
            cattleman.state,
            cattleman.document,
            cattleman.stateRegistration,
            cattleman.cnae
        )
    }

    data class CattleBalance(val balance: Int, val cattle: CattleType)

    data class FiscalDocument(
        val type: String?,
        val series: String?,
        val number: String?,
        val date: String,
        val cattleIn: Int,
        val cattleOut: Int,
        val supplier: Supplier?
    ) {
        data class Supplier(val name: String, val stateRegistration: String?, val state: String, val city: String) {
            constructor(supplier: Event.Supplier) : this(
                name = supplier.name,
                stateRegistration = supplier.stateRegistration,
                state = supplier.state,
                city = supplier.city
            )
        }

        constructor(event: Event) : this(
            type = event.fiscalDocumentType,
            series = event.fiscalDocumentSeries,
            number = event.fiscalDocumentNumber,
            date = event.date?.reportFormat() ?: "",
            cattleIn = event.cattleIn(),
            cattleOut = event.cattleOut(),
            supplier = event.supplier?.let(FiscalDocument::Supplier)
        )
    }
}