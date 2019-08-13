package io.github.matheusfm.dmg.domain.management.event.repository

import io.github.matheusfm.dmg.domain.management.event.Event
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.data.rest.core.annotation.RestResource
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.format.annotation.DateTimeFormat.ISO.DATE
import java.time.LocalDate

@RepositoryRestResource
interface EventRepository : PagingAndSortingRepository<Event, String> {

    @RestResource(path = "report")
    @Query(value = "{ 'cattleman.cattlemanId' : ?0, 'date' : { \$gte : ?1 , \$lte : ?2 } }")
    fun findByCattlemanAndDate(
        cattlemanId: String?,
        @DateTimeFormat(iso = DATE) dateFrom: LocalDate?,
        @DateTimeFormat(iso = DATE) dateTo: LocalDate?,
        pageable: Pageable
    ): Page<Event>

    @RestResource(exported = false)
    override fun <S : Event?> save(entity: S): S

    @RestResource(exported = false)
    override fun <S : Event?> saveAll(entities: MutableIterable<S>): MutableIterable<S>

    @RestResource(exported = false)
    override fun deleteById(id: String)

    @RestResource(exported = false)
    override fun deleteAll(entities: MutableIterable<Event>)

    @RestResource(exported = false)
    override fun deleteAll()

    @RestResource(exported = false)
    override fun delete(entity: Event)
}