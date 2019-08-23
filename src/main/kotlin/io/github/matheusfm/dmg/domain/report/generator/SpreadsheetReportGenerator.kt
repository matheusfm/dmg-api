package io.github.matheusfm.dmg.domain.report.generator

import org.jxls.common.Context
import org.jxls.util.JxlsHelper
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream

@Service
class SpreadsheetReportGenerator(
    @param:Value("classpath:report/template.xls") private val reportTemplate: Resource,
    private val jxls: JxlsHelper = JxlsHelper()
) : ReportGenerator {

    override fun generateReport(reportData: ReportData): ByteArray =
        reportTemplate.file.inputStream().use { inputStream ->
            ByteArrayOutputStream().use { outputStream ->
                jxls.processTemplate(inputStream, outputStream, Context(mapOf("data" to reportData)))
                return outputStream.toByteArray()
            }
        }
}