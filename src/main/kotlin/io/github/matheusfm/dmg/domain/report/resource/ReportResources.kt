package io.github.matheusfm.dmg.domain.report.resource

import io.github.matheusfm.dmg.domain.report.service.ReportService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.format.annotation.DateTimeFormat.ISO.DATE
import org.springframework.http.*
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/reports")
class ReportResources(val reportService: ReportService) {
    @GetMapping
    fun report(
        @RequestParam cattlemanId: String?,
        @RequestParam @DateTimeFormat(iso = DATE) dateFrom: LocalDate?,
        @RequestParam @DateTimeFormat(iso = DATE) dateTo: LocalDate?
    ): ResponseEntity<ByteArray> {

        val report = reportService.report(cattlemanId!!, dateFrom!!, dateTo!!)
        val headers = HttpHeaders()
        headers.contentType = MediaType("application", "vnd.ms-excel")
        headers.contentDisposition = ContentDisposition.builder("attachment").filename(report.fileName).build()
        return ResponseEntity(report.file, headers, HttpStatus.OK)
    }
}