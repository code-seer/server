package com.lms.modern.starter.config.api

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.lms.modern.starter.config.SystemConfigTestConfiguration
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.annotations.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


@SpringBootTest(classes = [SystemConfigTestConfiguration::class])
class SystemConfigTest: AbstractTestNGSpringContextTests() {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    @Qualifier("jacksonObjectMapper")
    lateinit var objectMapper: ObjectMapper

    @Test
    fun log_all_props() {
        val result = loadJson()
        assertNotNull(result)
        assertEquals(77, result.size)
        for (prop in result) {
            log.info("${prop.key}: ${prop.value}")
        }
    }

    private fun loadJson(): SortedMap<String, Any> {
        val jsonString = this.javaClass.getResource("/testProps.json").readText()
        val typeRef: TypeReference<HashMap<String, Any>> = object : TypeReference<HashMap<String, Any>>() {}
        val map = objectMapper.readValue(jsonString, typeRef)
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
                    props[newPrefix] = formatMultiVal(value)
                } else {
                    props[newPrefix] = value
                }
            } else {
                parseProps(props, child.value as HashMap<String, Any>, newPrefix)
            }
        }
    }

    private fun formatMultiVal(value: String): String {
        val tokens = value.split(",").sorted()
        var joiner = StringJoiner("\n")
        for (i in tokens.indices) {
            joiner.add("[${i}] ${tokens[i].replace("\n", "").trim()}")
        }
        return joiner.toString()
    }
}