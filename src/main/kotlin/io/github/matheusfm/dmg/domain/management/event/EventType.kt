package io.github.matheusfm.dmg.domain.management.event

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import io.github.matheusfm.dmg.domain.management.event.CattleType.*
import io.github.matheusfm.dmg.domain.management.event.CattleType.Companion.ALL

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class EventType(
    val text: String,
    @field:JsonIgnore val multiplier: Int,
    @field:JsonIgnore vararg val cattleTypes: CattleType,
    private val autoEventType: String? = null,
    private val autoEvent: ((CattleType) -> CattleType?)? = null
) {

    SLAUGHTER("Abate", -1, *ALL),

    IN("Entrada", 1, *ALL),

    INITIAL("Estoque inicial", 1, *ALL),

    DISCRIMINATION_OUT(
        "Mudança discriminação Saída",
        -1,
        BZO,
        GAR,
        BZA,
        NOV,
        autoEventType = "DISCRIMINATION_IN",
        autoEvent = CattleType::next
    ),

    DISCRIMINATION_IN(
        "Mudança discriminação Entrada",
        1,
        GAR,
        BOI,
        NOV,
        VAC,
        autoEventType = "DISCRIMINATION_OUT",
        autoEvent = CattleType::prev
    ),

    BIRTH("Nascimento", 1, BZO, BZA),

    LOSS("Perda", -1, *ALL),

    OUT("Saída", -1, *ALL);

    fun getValue() = name

    fun accept(cattleType: CattleType) = cattleType in cattleTypes

    fun autoEvent(cattleType: CattleType) = autoEvent?.invoke(cattleType)

    @JsonIgnore
    fun autoEventType() = autoEventType?.let(::valueOf)
}
