package com.lms.modern.starter.config.api


import com.fasterxml.jackson.core.type.TypeReference
import com.lms.modern.starter.util.lib.CustomObjectMapper
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*
import javax.annotation.PostConstruct


/**
 * Invoked at application startup to log service configurations. The output is
 * only valid at the time of the startup. Any updates made to the configuration
 * file on upstream will change the configurations at runtime.
 */
@Component
class SystemConfig(private val objectMapper: CustomObjectMapper) {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    private var activeProfile: String? = null
    private var appName: String? = null
    private var configServerUrl: String? = null


    /**
     * Log all configurations to the console
     */
    @PostConstruct
    fun logConfigs() {
        var maxNameLength = -1
        var maxValueLength = -1
        val defaultLength = 30
        var properties: SortedMap<String, Any>
        val propertyNames = ArrayList<String>()
        val configValues = ArrayList<Any>()
        try {
            activeProfile = System.getenv("SPRING_PROFILES_ACTIVE")
            appName = System.getenv("SPRING_APPLICATION_NAME")
            configServerUrl = System.getenv("SPRING_CLOUD_CONFIG_URI")
            properties = loadJsonHttp()
        } catch (e: ClientProtocolException) {
            log.warn("SPRING_PROFILES_ACTIVE not set")
            log.warn("SPRING_APPLICATION_NAME not set")
            log.warn("SPRING_CLOUD_CONFIG_URI not set")
            return
        }
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
            log.info(String.format("  | %-" + maxNameLength.toString() + "s : %s", propertyNames[i], configValues[i]))
        }
        log.info("  +$line")
    }

    @Throws(ClientProtocolException::class)
    private fun loadJsonHttp(): SortedMap<String, Any> {
        val httpClient = HttpClients.createDefault()
        val request = HttpGet("${configServerUrl}/${appName}-${activeProfile}.json")
        val typeRef: TypeReference<HashMap<String, Any>> = object : TypeReference<HashMap<String, Any>>() {}
        val map = objectMapper.o.readValue(httpClient.execute(request).entity.content, typeRef)
        var props: MutableMap<String, Any> = HashMap()
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