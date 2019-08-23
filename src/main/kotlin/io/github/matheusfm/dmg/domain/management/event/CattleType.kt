package io.github.matheusfm.dmg.domain.management.event

import com.fasterxml.jackson.annotation.JsonFormat

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class CattleType(
    val text: String,
    private val prev: String? = null,
    private val next: String? = null
) {
    BZO("Bezerro", next = "GAR"),
    GAR("Garrote", prev = "BZO", next = "BOI"),
    BOI("Boi", prev = "GAR"),
    BZA("Bezerra", next = "NOV"),
    NOV("Novilha", prev = "BZA", next = "VAC"),
    VAC("Vaca", prev = "NOV");

    fun prev() = prev?.let(::valueOf)

    fun next() = next?.let(::valueOf)

    companion object {
        @JvmField
        val ALL = values()
    }

    fun getValue() = name
}