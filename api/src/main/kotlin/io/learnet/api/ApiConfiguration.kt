package io.learnet.api

import io.learnet.data.migration.FlywayMigration
import io.learnet.service.ServiceConfiguration
import io.learnet.web.model.UserDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.event.EventListener
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.context.annotation.RequestScope
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


/**
 * Initialize the application by loading the seed data and performing any other
 * configurations necessary. Note that this process needs to happen exactly once
 * in a cloud environment. In order to ensure that it gets executed once, we
 * will check to see if the database has been seeded. Otherwise, we continue
 * with Flyway schema migration.
 */

@Configuration
@ComponentScan
@Import(value = [ServiceConfiguration::class])
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

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource? {
        val allowedOrigins: List<String> = listOf(
            "http://localhost:3000",
            "https://learnet.io")
        val allowedMethods: List<String> = listOf(
            "GET",
            "POST",
            "PUT",
            "DELETE",
            "OPTIONS"
        )
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = allowedOrigins
        configuration.allowedMethods = allowedMethods
        configuration.allowedHeaders = listOf("*")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}
