package io.github.matheusfm.dmg

import java.text.Normalizer
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy")

fun LocalDate.reportFormat(): String = this.format(pattern)

fun LocalDate.reportFormatUntil(localDate: LocalDate, separator: String = "a"): String =
    "${this.reportFormat()} $separator ${localDate.reportFormat()}"

fun String.normalize(): String = Normalizer.normalize(this, Normalizer.Form.NFD).replace("[^\\p{ASCII}]", "")