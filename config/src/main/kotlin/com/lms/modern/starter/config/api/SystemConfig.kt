package com.lms.modern.starter.config.api


import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.lang.reflect.Field
import java.util.ArrayList
import javax.annotation.PostConstruct
import kotlin.Array
import kotlin.String

/**
 * Invoked at application startup to log service configurations. The output is
 * only valid at the time of the startup. Any updates made to the configuration
 * file on upstream will change the configurations at runtime.
 */
@Component
class SystemConfig {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    /**
     * Parse out all instance fields and their values and log them to the console.
     */
    @PostConstruct
    fun logConfigs() {
        val fields: Array<Field> = SystemConfig::class.java.declaredFields
        val propertyNames = ArrayList<String>()
        val configValues = ArrayList<Any>()

        // Compute values for formatting console output
        val sinks = "kafkaConnectorSinks"
        var maxNameLength = -1
        var maxValueLength = -1
        val defaultLength = 30
        for (field in fields) {
            propertyNames.add(field.name)
            configValues.add(field.get(this))
            if (field.name != sinks) {
                maxNameLength = maxOf(maxNameLength, propertyNames.last().length, defaultLength)
                maxValueLength = maxOf(maxValueLength, configValues.last().toString().length, defaultLength)
            }
        }
        val excludedFields = arrayOf("log")
        val line = String(CharArray(maxValueLength + maxNameLength + 4)).replace("\u0000", "-")
        log.info("  +$line")
        log.info("  | ${applicationName} Configurations")
        log.info("  +$line")

        for (i in 0 until propertyNames.size) {
            if (!excludedFields.contains(propertyNames[i])) {
                if (propertyNames[i] == sinks) {
                    val sinkValues: MutableList<String> = configValues[i] as MutableList<String>
                    for (j in 0 until sinkValues.size) {
                        if (j == 0) {
                            log.info(String.format("  | %-" + maxNameLength.toString() + "s : [%s] %s", propertyNames[i], j, sinkValues[j]))
                        } else {
                            log.info(String.format("  | %-" + maxNameLength.toString() + "s : [%s] %s", "", j, sinkValues[j]))
                        }
                    }
                } else {
                    log.info(String.format("  | %-" + maxNameLength.toString() + "s : %s", propertyNames[i], configValues[i]))
                }
            }
        }
        log.info("  +$line")
    }

    /**
     * System configurations
     */
    @Value("\${description: ---description not found---}")
    private var description = ""

    @Value("\${source.name: ---Source name not found---}")
    private var source = ""

    @Value("\${source.profile: ---Profile name not found---}")
    var profile = ""

    @Value("\${server.port: ---Server port number not found---}")
    var serverPort = ""

    @Value("\${spring.application.name: ---Application name not found---}")
    var applicationName = ""

    @Value("\${eureka.client.serviceUrl.defaultZone: ---Eureka server not found---}")
    var eurekaServer = ""

    @Value("\${eureka.client.register-with-eureka: true }")
    var registerWithEureka: Boolean = false

    @Value("\${eureka.client.fetch-registry: true }")
    var fetchEurekaRegistry: Boolean = false

    @Value("\${server.servlet.context-path: ---Context path not found---}")
    var contextPath = ""

    @Value("\${logging.level.root: ---Root logging level not set---}")
    var rootLoggingLevel = ""

    @Value("\${http.schema: ---HTTP schema not found---}")
    var httpSchema = ""

    @Value("\${management.endpoints.web.exposure.include: ---management.endpoints.web.exposure.include---}")
    var httpTrace = ""

    /**
     * Database configurations
     */
//    @Value("\${spring.flyway.enabled: ---spring.flyway.enabled not set---}")
//    var flywayEnabled = ""
//
//    @Value("\${spring.flyway.locations: ---spring.flyway.locations not set---}")
//    var flywaySchemaLocation = ""
//
//    @Value("\${spring.flyway.baselineOnMigrate: ---spring.flyway.baselineOnMigrate not set---}")
//    var baselineOnMigrate = ""
//
//    @Value("\${spring.jpa.hibernate.ddl-auto: ---spring.jpa.hibernate.ddl-auto not set---}")
//    var jpaHibernateDdl = ""
//
//    @Value("\${spring.datasource.driver-class-name: ---Driver class name not found---}")
//    var driverClassName = ""
//
//    @Value("\${spring.datasource.url: ---Datasource url not found---}")
//    var datasourceUrl = ""
//
//    @Value("\${spring.datasource.username: ---Datasource username not found---}")
//    var datasourceUsername = ""
//
//    @Value("\${spring.datasource.password: ---Datasource password not found---}")
//    var datasourcePassword = ""
//
//    /**
//     * Kafka Connect configurations
//     */
//    @Value("\${kafka.connect.description: ---kafka.connect.description not found---}")
//    var kafkaConnectDesc = ""
//
//    @Value("\${kafka.connect.version: ---kafka.connect.version not found---}")
//    var kafkaConnectVersion = ""
//
//    @Value("\${kafka.connect.host: ---kafka.connect.host not found---}")
//    var kafkaConnectHost = ""
//
//    @Value("\${kafka.connect.port: ---kafka.connect.port not found---}")
//    var kafkaConnectPort = ""
//
//    @Value("\${kafka.connect.ping-sleep: ---kafka.connect.ping-sleep not found---}")
//    var kafkaConnectPingSleep = ""
//
//    /**
//     * Kafka Connector configurations
//     */
//    @Value("\${kafka.connect.connector.prefix: ---kafka.connect.connector.prefix not found---}")
//    var kafkaConnectorPrefix = ""
//
//    @Value("\${kafka.connect.connector.source.suffix: ---kafka.connect.connector.source.suffix not found---}")
//    var kafkaConnectorSourceSuffix = ""
//
//    @Value("\${kafka.connect.connector.source.topic-prefix: ---kafka.connect.connector.source.topic-prefix not found---}")
//    var kafkaConnectorSourceTopicPrefix = ""
//
//    @Value("\${kafka.connect.connector.source.schema-whitelist: ---kafka.connect.connector.source.schema-whitelist not found---}")
//    var kafkaConnectorSourceSchemaWhitelist = ""
//
//    @Value("\${kafka.connect.connector.source.connector-class: ---kafka.connect.connector.source.connector-class not found---}")
//    var kafkaConnectorSourceConnectorClass = ""
//
//    @Value("\${kafka.connect.connector.source.database.server-name: ---kafka.connect.connector.source.database.server-name not found---}")
//    var kafkaConnectorSourceDatabaseServerName = ""
//
//    @Value("\${kafka.connect.connector.source.database.host: ---kafka.connect.connector.source.database.host not found---}")
//    var kafkaConnectorSourceDatabaseHost = ""
//
//    @Value("\${kafka.connect.connector.source.database.port: ---kafka.connect.connector.source.database.port not found---}")
//    var kafkaConnectorSourceDatabasePort = ""
//
//    @Value("\${kafka.connect.connector.source.database.username: ---kafka.connect.connector.source.database.username not found---}")
//    var kafkaConnectorSourceDatabaseUsername = ""
//
//    @Value("\${kafka.connect.connector.source.database.password: ---kafka.connect.connector.source.database.password not found---}")
//    var kafkaConnectorSourceDatabasePassword = ""
//
//    @Value("\${kafka.connect.connector.source.num-source: ---kafka.connect.connector.source.num-source not found---}")
//    var kafkaConnectorSourceNumSource = ""
//
//    @Value("\${kafka.connect.connector.source.source-data.name: ---kafka.connect.connector.source.source-data.name not found---}")
//    var kafkaConnectorDataSourceName = ""
//
//    @Value("\${kafka.connect.connector.source.source-data.tasks-max: ---kafka.connect.connector.source.source-data.tasks-max not found---}")
//    var kafkaConnectorDataSourceTasksMax = ""
//
//    @Value("\${kafka.connect.connector.source.source-data.dbname: ---kafka.connect.connector.source.source-data.dbname not found---}")
//    var kafkaConnectorDataSourceDbname = ""
//
//    @Value("\${kafka.connect.connector.source.source-logging.name: ---kafka.connect.connector.source.source-logging.name not found---}")
//    var kafkaConnectorLoggingSourceName = ""
//
//    @Value("\${kafka.connect.connector.source.source-logging.tasks-max: ---kafka.connect.connector.source.source-logging.tasks-max not found---}")
//    var kafkaConnectorLoggingSourceTasksMax = ""
//
//    @Value("\${kafka.connect.connector.source.source-logging.dbname: ---kafka.connect.connector.source.source-logging.dbname not found---}")
//    var kafkaConnectorLoggingSourceDbname = ""
//
//    @Value("\${kafka.connect.connector.sink.suffix: ---kafka.connect.connector.sink.suffix not found---}")
//    var kafkaConnectorSinkSuffix = ""
//
//    @Value("\${kafka.connect.connector.sink.host: ---kafka.connect.connector.sink.host not found---}")
//    var kafkaConnectorSinkConnectorHost = ""
//
//    @Value("\${kafka.connect.connector.sink.connector-class: ---kafka.connect.connector.sink.connector-class not found---}")
//    var kafkaConnectorSinkConnectorClass = ""
//
//    @Value("\${kafka.connect.connector.sink.transforms: ---kafka.connect.connector.sink.transforms not found---}")
//    var kafkaConnectorSinkTransforms = ""
//
//    @Value("\${kafka.connect.connector.sink.transforms-unwrap-type: ---kafka.connect.connector.sink.transforms-unwrap-type not found---}")
//    var kafkaConnectorSinkTransformsUnwrapType = ""
//
//    @Value("\${kafka.connect.connector.sink.transforms-unwrap-drop-tombstones: ---kafka.connect.connector.sink.transforms-unwrap-drop-tombstones not found---}")
//    var kafkaConnectorSinkTransformsUnwrapDropTombstones = ""

//    @Value("\${kafka.connect.connector.sink.transforms-key-type: ---kafka.connect.connector.sink.transforms-key-type not found---}")
//    var kafkaConnectorSinkTransformsKeyType = ""
//
//    @Value("\${kafka.connect.connector.sink.transforms-key-field: ---kafka.connect.connector.sink.transforms-key-field not found---}")
//    var kafkaConnectorSinkTransformsKeyField = ""
//
//    @Value("\${kafka.connect.connector.sink.behavior-on-null-values: ---kafka.connect.connector.sink.behavior-on-null-values not found---}")
//    var kafkaConnectorSinkBehaviorOnNullValues = ""
//
//    @Value("\${kafka.connect.connector.sink.key-ignore: ---kafka.connect.connector.sink.key-ignore not found---}")
//    var kafkaConnectorSinkKeyIgnore = ""
//
//    @Value("\${kafka.connect.connector.sink.tasks-max: ---kafka.connect.connector.sink.tasks-max not found---}")
//    var kafkaConnectorSinkTasksMax = ""
//
//    @Value("\${kafka.connect.connector.sink.sinks}")
//    var kafkaConnectorSinks: MutableList<String> = ArrayList()


    /**
     * Elasticsearch configurations
     */

//    @Value("\${elasticsearch.indexSyncDelay: -1}")
//    var indexSyncDelay = -1L
//
//    @Value("\${elasticsearch.version: ---elasticsearch.version not found---}")
//    var elasticsearchVersion = ""
//
//    @Value("\${elasticsearch.host: ---elasticsearch.host not found---}")
//    var elasticsearchHost = ""
//
//    @Value("\${elasticsearch.port: ---elasticsearch.port not found---}")
//    var elasticsearchPort = 0
//
//    @Value("\${elasticsearch.index.course: ---course index not found---}")
//    var courseIndex = ""
//
//    @Value("\${elasticsearch.index.prerequisite: ---prerequisite index not found---}")
//    var prerequisiteIndex = ""
//
//    @Value("\${elasticsearch.index.faq: ---faq index not found---}")
//    var faqIndex = ""
//
//    @Value("\${elasticsearch.index.instruction_mode: ---instruction_mode index not found---}")
//    var instructionModeIndex = ""
//
//    @Value("\${elasticsearch.index.course_curriculum_bridge: ---course_curriculum_bridge index not found---}")
//    var courseCurriculumBridgeIndex = ""
//
//    @Value("\${elasticsearch.index.institution: ---institution index not found---}")
//    var institutionIndex = ""
//
//    @Value("\${elasticsearch.index.curriculum: ---curriculum index not found---}")
//    var curriculumIndex = ""
//
//    @Value("\${elasticsearch.index.curriculum_outcome_bridge: ---curriculum_outcome_bridge index not found---}")
//    var curriculumOutcomeBridgeIndex = ""
//
//    @Value("\${elasticsearch.index.assignment: ---assignment index not found---}")
//    var assignmentIdex = ""
//
//    @Value("\${elasticsearch.index.course_instructor_bridge: ---course_instructor_bridge index not found---}")
//    var courseInstructorBridgeIndex = ""
//
//    @Value("\${elasticsearch.index.subject: ---subject index not found---}")
//    var subjectIndex = ""
//
//    @Value("\${elasticsearch.index.course_language_bridge: ---course_language_bridge index not found---}")
//    var courseLanguageBridgeIndex = ""
//
//    @Value("\${elasticsearch.index.content: ---content index not found---}")
//    var contentIndex = ""
//
//    @Value("\${elasticsearch.index.language: ---language index not found---}")
//    var languageIndex = ""
//
//    @Value("\${elasticsearch.index.outcome: ---outcome index not found---}")
//    var outcomeIndex = ""
//
//    @Value("\${elasticsearch.index.curriculum_instruction_mode_bridge: ---curriculum_instruction_mode_bridge index not found---}")
//    var curriculumInstructionModeBridgeIndex = ""
//
//    @Value("\${elasticsearch.index.course_instruction_mode_bridge: ---course_instruction_mode_bridge index not found---}")
//    var courseInstructionModeBridgeIndex = ""
//
//    @Value("\${elasticsearch.index.course_enrollment_bridge: ---course_enrollment_bridge index not found---}")
//    var courseEnrollmentBridgeIndex = ""
}