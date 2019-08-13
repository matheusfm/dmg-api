package io.github.matheusfm.dmg.domain.management.event

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import io.github.matheusfm.dmg.domain.management.event.CattleType.*
import io.github.matheusfm.dmg.domain.management.event.CattleType.Companion.ALL

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class EventType(
    val text: String,
    @field:JsonIgnore vararg val cattleTypes: CattleType
) {

    SLAUGHTER("Abate", *ALL),

    INITIAL("Estoque inicial", *ALL),

    DISCRIMINATION_IN("Mudança discriminação Entrada", G, B, N, V),

    DISCRIMINATION_OUT("Mudança discriminação Saída", BO, G, BA, N),

    BIRTH("Nascimento", BO, BA),

    LOSS("Perda", *ALL),

    OUT("Saída", *ALL);

    fun getValue() = name

    fun accept(cattleType: CattleType) = cattleType in cattleTypes
}
