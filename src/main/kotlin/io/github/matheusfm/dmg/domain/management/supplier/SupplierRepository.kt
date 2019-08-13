package io.github.matheusfm.dmg.domain.management.supplier

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.data.rest.core.annotation.RestResource

@RepositoryRestResource
interface SupplierRepository : PagingAndSortingRepository<Supplier, String> {

    @RestResource(path = "text", rel = "text")
    @Query("{ \$text: { \$search: ?0 } }")
    fun findByText(text: String?, pageable: Pageable): Page<Supplier>
}