package com.lms.modern.starter.data.migration

import com.lms.modern.starter.data.DataConfiguration
import com.lms.modern.starter.util.lib.line
import org.elasticsearch.action.admin.indices.cache.clear.ClearIndicesCacheRequest
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.client.indices.GetIndexRequest
import org.flywaydb.core.Flyway
import org.hibernate.search.mapper.orm.Search
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext


/**
 * Migrate schema and performs bulk indexing for Elasticsearch.
 */
@Transactional
@Component
open class FlywayMigrationImpl(
        private val dataConfig: DataConfiguration
): FlywayMigration {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)
    private val maxNameLength = 60

    @PersistenceContext
    lateinit var entityManager: EntityManager

    private val flyway = Flyway
            .configure()
            .schemas(dataConfig.schemas)
            .validateOnMigrate(true)
            .baselineOnMigrate(true)
            .dataSource(dataConfig.datasourceUrl,
                    dataConfig.datasourceUsername,
                    dataConfig.datasourcePassword)

    override fun init() {
        log.info("  +$line")
        log.info("  | Flyway Schema Migration")
        log.info("  +$line")
        clean()
        flyway.locations(dataConfig.flywaySchemaLocation).load().migrate()
        indexData()
    }

    override fun clean() {
        if (dataConfig.profile == "dev" || dataConfig.applicationName == "starter") {
            log.info(String.format("  | %-" + maxNameLength.toString() + "s : %s", "Dropping database", "true"))
            Flyway(flyway).clean()
        } else {
            log.info(String.format("  | %-" + maxNameLength.toString() + "s : %s", "Dropping database", "false"))
        }
    }

    override fun indexData() {
        log.info("Bulk indexing in progress")
        val searchSession = Search.session(entityManager)
        searchSession.massIndexer().startAndWait()

    }

}