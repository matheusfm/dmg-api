package io.github.matheusfm.dmg.domain.management.event

import com.fasterxml.jackson.annotation.JsonFormat

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class CattleType(val text: String) {
    BO("Bezerro"),
    G("Garrote"),
    B("Boi"),
    BA("Bezerra"),
    N("Novilha"),
    V("Vaca");

    companion object {
        @JvmField
        val ALL = values()
    }

    fun getValue() = name
}