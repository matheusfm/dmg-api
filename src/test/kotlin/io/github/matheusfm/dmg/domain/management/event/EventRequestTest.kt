package io.github.matheusfm.dmg.domain.management.event

import org.junit.Assert.*
import org.junit.Test

class EventRequestTest {
    @Test
    fun invalidCattleTest() {
        val eventRequest = EventRequest(
            eventType = EventType.BIRTH,
            cattle = mapOf(
                CattleType.BA to 10,
                CattleType.B to 1,
                CattleType.BO to 10
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
        assertEquals(CattleType.B, violations.first())
    }
}