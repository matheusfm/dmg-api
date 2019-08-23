package io.github.matheusfm.dmg.domain.report.generator

interface ReportGenerator {
    fun generateReport(reportData: ReportData): ByteArray
}