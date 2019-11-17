package io.github.matheusfm.dmg.domain.management.cattleman

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.data.rest.core.annotation.RestResource
import org.springframework.security.access.prepost.PreAuthorize

@RepositoryRestResource
interface CattlemanRepository : PagingAndSortingRepository<Cattleman, String> {

    @RestResource(path = "text", rel = "text")
    @Query("{ \$or: [ { 'name' : { \$regex: '.*?0.*', \$options: 'i' } }, { 'document' : { \$regex: '.*?0.*' } }, { 'code' : { \$regex: '.*?0.*' } } ] }")
    fun findByText(text: String?, pageable: Pageable): Page<Cattleman>

    @PreAuthorize("hasRole('ADMIN')")
    override fun deleteById(id: String)

    @PreAuthorize("hasRole('ADMIN')")
    override fun deleteAll(entities: MutableIterable<Cattleman>)

    @PreAuthorize("hasRole('ADMIN')")
    override fun deleteAll()

    @PreAuthorize("hasRole('ADMIN')")
    override fun delete(entity: Cattleman)
}