package io.learnet.starter.api

import io.learnet.starter.util.properties.DemoUserProps
import io.learnet.starter.api.security.createClaims
import io.learnet.starter.api.security.createUser
import io.learnet.starter.api.security.deleteUser
import io.learnet.starter.data.migration.FlywayMigration
import io.learnet.starter.data.repo.DemoUserRepo
import io.learnet.starter.search.SearchConfiguration
import io.learnet.starter.search.api.SearchApi
import io.learnet.starter.service.ServiceConfiguration
import org.elasticsearch.client.core.CountRequest
import org.elasticsearch.index.query.QueryBuilders
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.annotation.*
import org.springframework.context.event.EventListener
import org.springframework.dao.InvalidDataAccessResourceUsageException


/**
 * Initialize the application by loading the seed data and performing any other
 * configurations necessary. Note that this process needs to happen exactly once
 * in a cloud environment. In order to ensure that it gets executed once, we
 * will check to see if the database has been seeded. Otherwise, we continue
 * with Flyway schema migration.
 */

//@ComponentScan(excludeFilters = [ComponentScan.Filter(
//        type = FilterType.ASSIGNABLE_TYPE,
//        classes = arrayOf(OpenApiControllerWebMvc::class))])
@Configuration
@ComponentScan
@Import(value = [ SearchConfiguration::class, ServiceConfiguration::class ] )
class ApiConfiguration(private val demoUserRepo: DemoUserRepo) {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    lateinit var flywayMigration: FlywayMigration

    @Autowired
    private lateinit var demoUserProps: DemoUserProps

    @Autowired
    lateinit var searchApi: SearchApi


    /**
     * TODO: Run the comparison on all tables and indices that should be seeded.
     */
    @EventListener
    fun onApplicationStart(event: ApplicationStartedEvent) {
        handleMigration()
    }


    private fun handleMigration() {
        val countRequest = CountRequest(arrayOf(demoUserProps.esIndex), QueryBuilders.matchAllQuery())
        val response = searchApi.count(countRequest)
        try {
            if (demoUserRepo.findAll().size == 0 || response.count == 0L) {
                flywayMigration.init()
            }
        }
        catch (e: InvalidDataAccessResourceUsageException) {
            log.error(e.message)
            flywayMigration.init()
        }
    }
}
