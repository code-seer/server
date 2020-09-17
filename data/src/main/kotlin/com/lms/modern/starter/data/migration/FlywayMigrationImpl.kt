package com.lms.modern.starter.data.migration

import com.lms.modern.course.config.api.SystemConfig
import com.lms.modern.course.util.lib.line
import org.flywaydb.core.Flyway
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class FlywayMigrationImpl(private val systemConfig: SystemConfig) : FlywayMigration {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)
    private val maxNameLength = 60

    private val flyway = Flyway
            .configure()
            .schemas(systemConfig.kafkaConnectorSourceSchemaWhitelist)
            .validateOnMigrate(true)
            .baselineOnMigrate(true)
            .dataSource(systemConfig.datasourceUrl,
                    systemConfig.datasourceUsername,
                    systemConfig.datasourcePassword)

    override fun init() {
        log.info("  +$line")
        log.info("  | Flyway Schema Migration")
        log.info("  +$line")
        flyway.locations(systemConfig.flywaySchemaLocation).load().migrate()
    }

    override fun clean() {
        if (systemConfig.profile == "dev") {
            log.info(String.format("  | %-" + maxNameLength.toString() + "s : %s", "Dropping database", "true"))
            Flyway(flyway).clean()
        } else {
            log.info(String.format("  | %-" + maxNameLength.toString() + "s : %s", "Dropping database", "false"))
        }
    }

}