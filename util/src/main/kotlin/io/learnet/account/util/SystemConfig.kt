package io.learnet.account.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import java.util.*
import javax.annotation.PostConstruct
import kotlin.collections.HashMap


/**
 * Invoked at application startup to log service configurations. The output is
 * only valid at the time of the startup. Any updates made to the configuration
 * file on upstream will change the configurations at runtime.
 */
@Component
class SystemConfig(private val env: Environment) {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)
    private val excludedProps = arrayOf(
        "firebase.private_key",
        "s3.key",
        "s3.secret"
    )
    private val allPropKeys = arrayOf(
        "demouser.es_index",
        "demouser.display_name",
        "demouser.email",
        "demouser.password",
        "demouser.phone_number",
        "demouser.photo_url",
        "description",
        "firebase.type",
        "firebase.project_id",
        "firebase.private_key_id",
        "firebase.private_key",
        "firebase.client_email",
        "firebase.client_id",
        "firebase.auth_uri",
        "firebase.token_uri",
        "firebase.auth_provider_x509_cert_url",
        "firebase.client_x509_cert_url",
        "firebase.custom_token_verification_url",
        "firebase.client_api_key",
        "http.schema",
        "logging.level.org.springframework.security",
        "logging.level.root",
        "s3.key",
        "s3.secret",
        "s3.user",
        "s3.bucket",
        "server.port",
        "server.servlet.context-path",
        "source.profile",
        "spring.application.name",
        "spring.profiles.active",
        "spring.http.multipart.enabled",
        "spring.servlet.multipart.max-request-size",
        "spring.servlet.multipart.max-file-size",
        "spring.servlet.multipart.enabled",
        "spring.jpa.hibernate.ddl-auto",
        "spring.flyway.locations",
        "spring.flyway.baselineOnMigrate",
        "spring.flyway.enabled",
        "spring.flyway.schemas",
        "spring.datasource.driver-class-name",
        "spring.datasource.url",
        "spring.datasource.username",
        "spring.datasource.password",
        "spring.datasource.main.banner-mode",
        "user.permissions.uiAuthorizedEmails",
        "user.permissions.defaultPermissions"
    )

    /**
     * Log all configurations to the console
     */
    @PostConstruct
    fun logConfigs() {
        var maxNameLength = -1
        var maxValueLength = -1
        val defaultLength = 30
        var properties: SortedMap<String, Any> = loadProperties()
        val propertyNames = ArrayList<String>()
        val configValues = ArrayList<Any>()
        for (field in properties) {
            if (field.key != "description") {
                propertyNames.add(field.key)
                configValues.add(field.value)
                maxNameLength = maxOf(maxNameLength, propertyNames.last().length, defaultLength)
                maxValueLength = maxOf(maxValueLength, configValues.last().toString().length, defaultLength)
            }
        }
        val description = properties["description"]
        val lineLength = description.toString().length *2
        val line = String(CharArray(lineLength)).replace("\u0000", "-")
        log.info("  +$line")
        log.info("  | $description")
        log.info("  +$line")
        for (i in 0 until propertyNames.size) {
            if (!excludedProps.contains(propertyNames[i])) {
                log.info(
                    String.format(
                        "  | %-" + maxNameLength.toString() + "s : %s",
                        propertyNames[i],
                        configValues[i]
                    )
                )
            }
        }
        log.info("  +$line")
    }

    private fun loadProperties(): SortedMap<String, Any> {
        val map: HashMap<String, Any> = HashMap()
        val props: MutableMap<String, Any> = HashMap()
        allPropKeys.forEach {
            val value = env.getProperty(it)
            if (value != null) {
                map[it] = value
            }}
        parseProps(props, map, String())
        return props.toSortedMap()
    }

    private fun parseProps(props: MutableMap<String, Any>, map: HashMap<String, Any>, prefix: String) {
        for (child in map) {
            val newPrefix = if (prefix.isNotEmpty()) {
                "${prefix}.${child.key}"
            } else {
                child.key
            }
            if (child.value !is HashMap<*, *>) {
                val value = child.value
                if (value is String && value.split(",").size > 1)  {
                    props[newPrefix] = cleanupMultiVal(value)
                } else {
                    props[newPrefix] = value
                }
            } else {
                parseProps(props, child.value as HashMap<String, Any>, newPrefix)
            }
        }
    }

    private fun cleanupMultiVal(value: String): MutableList<String> {
        val tokens = value.split(",").sorted()
        val list = arrayListOf<String>()
        for (token in tokens) {
            list.add(token.replace("\n", "").trim())
        }
        return list
    }
}
