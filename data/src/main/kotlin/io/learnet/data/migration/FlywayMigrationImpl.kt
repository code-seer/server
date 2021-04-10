package io.learnet.data.migration

import io.learnet.util.lib.line
import io.learnet.util.properties.DataSourceProps
import io.learnet.util.properties.FlywayProps
import io.learnet.util.properties.SourceProps
import io.learnet.util.properties.SpringApplicationProps
import org.flywaydb.core.Flyway
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
    private val dataSourceProps: DataSourceProps,
    private val sourceProps: SourceProps,
    private val springAppProps: SpringApplicationProps,
    private val flywayProps: FlywayProps
): FlywayMigration {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)
    private val maxNameLength = 60

    @PersistenceContext
    lateinit var entityManager: EntityManager

    private val flyway = Flyway
            .configure()
            .schemas(flywayProps.schemas)
            .validateOnMigrate(true)
            .baselineOnMigrate(true)
            .dataSource(dataSourceProps.url,
                    dataSourceProps.username,
                    dataSourceProps.password)

    override fun init() {
        log.info("  +$line")
        log.info("  | Flyway Schema Migration")
        log.info("  +$line")
        clean()
        flyway.locations(flywayProps.locations).load().migrate()
    }

    override fun clean() {
        if (sourceProps.profile == "dev" || springAppProps.name == "starter") {
            log.info(String.format("  | %-" + maxNameLength.toString() + "s : %s", "Dropping database", "true"))
            Flyway(flyway).clean()
        } else {
            log.info(String.format("  | %-" + maxNameLength.toString() + "s : %s", "Dropping database", "false"))
        }
    }

}
