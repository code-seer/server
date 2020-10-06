package com.lms.modern.starter.api

import com.lms.modern.starter.api.properties.FirebaseProps
import com.lms.modern.starter.api.properties.SecurityProps
import com.lms.modern.starter.api.security.createClaims
import com.lms.modern.starter.api.security.createUser
import com.lms.modern.starter.api.security.deleteUser
import com.lms.modern.starter.data.migration.FlywayMigration
import com.lms.modern.starter.data.repo.DemoUserRepo
import com.lms.modern.starter.search.SearchConfiguration
import com.lms.modern.starter.search.api.SearchApi
import com.lms.modern.starter.service.ServiceConfiguration
import org.elasticsearch.client.core.CountRequest
import org.elasticsearch.index.query.QueryBuilders
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.boot.context.properties.EnableConfigurationProperties
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
@EnableConfigurationProperties(SecurityProps::class, FirebaseProps::class)
class ApiConfiguration(private val demoUserRepo: DemoUserRepo) {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    lateinit var flywayMigration: FlywayMigration

    @Autowired
    lateinit var searchApi: SearchApi

    private val testIndex = "lms-demo_user-read"


    /**
     * TODO: Run the comparison on all tables and indices that should be seeded.
     */
    @EventListener
    fun onApplicationStart(event: ApplicationStartedEvent) {
        handleMigration()
        createDemoUser()
    }

    /**
     * Create a demo LMS user account to access protected services. This microservice
     * is not publicly accessible. It can only be accessed through the API Gateway
     * and the user must be authenticated. Otherwise a 401 response is returned.
     */
    private fun createDemoUser() {
        deleteUser()
        createClaims(createUser(), false)
    }

    private fun handleMigration() {
        val countRequest = CountRequest(arrayOf(testIndex), QueryBuilders.matchAllQuery())
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