package io.github.matheusfm.dmg.domain.management.cattleman

import io.github.matheusfm.dmg.domain.management.DocumentType
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.TextIndexed
import org.springframework.data.mongodb.core.mapping.Document
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

@Document(collection = "cattlemen", language = "portuguese")
data class Cattleman(
    @field:Id val id: String?,
    @field:TextIndexed @field:NotBlank val name: String?,
    @field:Pattern(regexp = "(\\d{5}[-]?\\d{3})") val zipCode: String?,
    @field:NotBlank val address: String?,
    @field:NotBlank val city: String?,
    @field:NotBlank val state: String?,
    @field:TextIndexed @field:NotBlank val document: String?,
    @field:NotNull val documentType: DocumentType?,
    val stateRegistration: String?,
    val cnae: String?,
    val reporter: String?
)