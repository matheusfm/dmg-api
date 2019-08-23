package io.github.matheusfm.dmg.domain.management.event

import org.junit.Assert.*
import org.junit.Test
import java.util.*

class AutoEventTest {
    @Test
    fun discriminationInTest() {
        val event = Event(
            id = "eventId",
            eventType = EventType.DISCRIMINATION_IN,
            cattle = listOf(Event.Cattle(CattleType.BOI, 2)),
            cattleman = Event.Cattleman(UUID.randomUUID().toString(), "Pecuarista", "Documento")
        )

        val autoEvent = event.autoEvent()
        assertNotNull(autoEvent)
        assertEquals(EventType.DISCRIMINATION_OUT, autoEvent?.eventType)
        assertNotNull(autoEvent?.cattle)
        assertEquals(1, autoEvent?.cattle?.size)
        assertEquals(Event.Cattle(CattleType.GAR, 2), autoEvent?.cattle?.first())
        assertNotNull(autoEvent?.originalEventId)
        assertEquals("eventId", autoEvent?.originalEventId)
        assertNull(autoEvent?.id)
    }

    @Test
    fun discriminationOutTest() {
        val event = Event(
            id = "eventId",
            eventType = EventType.DISCRIMINATION_OUT,
            cattle = listOf(Event.Cattle(CattleType.NOV, 4)),
            cattleman = Event.Cattleman(UUID.randomUUID().toString(), "Pecuarista", "Documento")
        )

        val autoEvent = event.autoEvent()
        assertNotNull(autoEvent)
        assertEquals(EventType.DISCRIMINATION_IN, autoEvent?.eventType)
        assertNotNull(autoEvent?.cattle)
        assertEquals(1, autoEvent?.cattle?.size)
        assertEquals(Event.Cattle(CattleType.VAC, 4), autoEvent?.cattle?.first())
        assertNotNull(autoEvent?.originalEventId)
        assertEquals("eventId", autoEvent?.originalEventId)
        assertNull(autoEvent?.id)
    }

    @Test
    fun autoEventNullTest() {
        val event = Event(
            eventType = EventType.OUT,
            cattle = listOf(Event.Cattle(CattleType.VAC, 7)),
            cattleman = Event.Cattleman(UUID.randomUUID().toString(), "Pecuarista", "Documento")
        )

        assertNull(event.autoEvent())
    }
}