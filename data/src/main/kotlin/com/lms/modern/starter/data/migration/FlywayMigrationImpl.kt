package com.lms.modern.starter.data.migration

import com.lms.modern.starter.data.DataConfiguration
import com.lms.modern.starter.util.lib.line
import org.flywaydb.core.Flyway
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class FlywayMigrationImpl(private val dataConfig: DataConfiguration): FlywayMigration {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)
    private val maxNameLength = 60

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
        flyway.locations(dataConfig.flywaySchemaLocation).load().migrate()
    }

    override fun clean() {
        if (dataConfig.profile == "dev") {
            log.info(String.format("  | %-" + maxNameLength.toString() + "s : %s", "Dropping database", "true"))
            Flyway(flyway).clean()
        } else {
            log.info(String.format("  | %-" + maxNameLength.toString() + "s : %s", "Dropping database", "false"))
        }
    }

}