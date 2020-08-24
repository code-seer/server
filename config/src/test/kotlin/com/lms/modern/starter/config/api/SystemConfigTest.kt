package com.lms.modern.starter.config.api


import com.fasterxml.jackson.core.type.TypeReference
import com.lms.modern.starter.config.SystemConfigTestConfiguration
import com.lms.modern.starter.util.lib.CustomObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.annotations.Test
import java.util.*
import kotlin.collections.LinkedHashMap


@SpringBootTest(classes = [SystemConfigTestConfiguration::class])
class SystemConfigTest: AbstractTestNGSpringContextTests() {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    lateinit var objectMapper: CustomObjectMapper

    @Test
    fun log_all_props() {
        loadJson()
        log.info("this is a fun test!")
    }

    private fun loadJson() {
        val jsonString = this.javaClass.getResource("/testProps.json").readText()
        val typeRef: TypeReference<HashMap<String, Any>> = object : TypeReference<HashMap<String, Any>>() {}
        val map = objectMapper.o.readValue(jsonString, typeRef)
        var props: MutableMap<String, Any> = HashMap()
        parseProps(props, map, String())
        props = props.toSortedMap()
        for (prop in props) {
         log.info("${prop.key}: ${prop.value}")
        }
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
                if (value is String && (value as String).split(",").size > 1)  {
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