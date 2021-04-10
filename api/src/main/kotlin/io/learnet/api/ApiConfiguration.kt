package io.learnet.api

import io.learnet.data.migration.FlywayMigration
import io.learnet.web.model.UserDto
import io.learnet.service.ServiceConfiguration
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.annotation.*
import org.springframework.context.event.EventListener
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.context.annotation.RequestScope


/**
 * Initialize the application by loading the seed data and performing any other
 * configurations necessary. Note that this process needs to happen exactly once
 * in a cloud environment. In order to ensure that it gets executed once, we
 * will check to see if the database has been seeded. Otherwise, we continue
 * with Flyway schema migration.
 */

@Configuration
@ComponentScan
@Import(value = [ServiceConfiguration::class ] )
class ApiConfiguration {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    lateinit var flywayMigration: FlywayMigration

    @Bean
    @RequestScope
    fun userDto(): UserDto {
        return SecurityContextHolder.getContext().authentication?.principal as UserDto
    }

    /**
     * TODO: Run the comparison on all tables and indices that should be seeded.
     */
    @EventListener
    fun onApplicationStart(event: ApplicationStartedEvent) {
        flywayMigration.init()
    }
}
