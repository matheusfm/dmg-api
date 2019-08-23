package io.github.matheusfm.dmg.domain.management.event

import io.github.matheusfm.dmg.domain.management.event.resource.EventRequest
import org.junit.Assert.*
import org.junit.Test

class EventRequestTest {
    @Test
    fun invalidCattleTest() {
        val eventRequest = EventRequest(
            eventType = EventType.BIRTH,
            cattle = mapOf(
                CattleType.BZA to 10,
                CattleType.BOI to 1,
                CattleType.BZO to 10
            ),
            cattlemanId = null,
            date = null,
            fiscalDocumentType = null,
            fiscalDocumentSeries = null,
            fiscalDocumentNumber = null,
            supplierId = null
        )

        val violations = eventRequest.getCattleViolations()

        assertFalse(eventRequest.isCattleValid())
        assertTrue(violations.isNotEmpty())
        assertEquals(CattleType.BOI, violations.first())
    }
}