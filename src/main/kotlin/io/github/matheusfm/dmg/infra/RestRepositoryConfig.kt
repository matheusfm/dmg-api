package io.github.matheusfm.dmg.infra

import io.github.matheusfm.dmg.domain.management.cattleman.Cattleman
import io.github.matheusfm.dmg.domain.management.event.Event
import io.github.matheusfm.dmg.domain.management.supplier.Supplier
import org.springframework.data.rest.core.config.RepositoryRestConfiguration
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer
import org.springframework.stereotype.Component
import org.springframework.validation.Validator

@Component
class RestRepositoryConfig(val validator: Validator) : RepositoryRestConfigurer {
    override fun configureRepositoryRestConfiguration(config: RepositoryRestConfiguration?) {
        config?.corsRegistry?.addMapping("/**")
        config?.exposeIdsFor(Supplier::class.java, Cattleman::class.java, Event::class.java)
        config?.exposureConfiguration?.forDomainType(Event::class.java)
    }

    override fun configureValidatingRepositoryEventListener(validatingListener: ValidatingRepositoryEventListener?) {
        validatingListener?.addValidator("beforeCreate", validator)
        validatingListener?.addValidator("beforeSave", validator)
    }
}