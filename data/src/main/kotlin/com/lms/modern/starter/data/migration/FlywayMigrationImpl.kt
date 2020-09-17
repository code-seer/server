package com.lms.modern.starter.data.migration

import com.lms.modern.course.util.lib.line
import org.flywaydb.core.Flyway
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class FlywayMigrationImpl: FlywayMigration {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)
    private val maxNameLength = 60

    @Value("\${source.profile: ---Profile name not found---}")
    var profile = ""

    @Value("\${spring.datasource.url: ---Datasource url not found---}")
    var datasourceUrl = ""

    @Value("\${spring.datasource.username: ---Datasource username not found---}")
    var datasourceUsername = ""

    @Value("\${spring.datasource.password: ---Datasource password not found---}")
    var datasourcePassword = ""

    @Value("\${spring.data.schemas: ---Data schemas not found---}")
    var schemas = ""

    @Value("\${spring.flyway.locations: ---spring.flyway.locations not set---}")
    var flywaySchemaLocation = ""

    private val flyway = Flyway
            .configure()
            .schemas(schemas)
            .validateOnMigrate(true)
            .baselineOnMigrate(true)
            .dataSource(datasourceUrl,
                    datasourceUsername,
                    datasourcePassword)

    override fun init() {
        log.info("  +$line")
        log.info("  | Flyway Schema Migration")
        log.info("  +$line")
        flyway.locations(flywaySchemaLocation).load().migrate()
    }

    override fun clean() {
        if (profile == "dev") {
            log.info(String.format("  | %-" + maxNameLength.toString() + "s : %s", "Dropping database", "true"))
            Flyway(flyway).clean()
        } else {
            log.info(String.format("  | %-" + maxNameLength.toString() + "s : %s", "Dropping database", "false"))
        }
    }

}