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
        val props: MutableMap<String, Any> = HashMap()
        parseProps(props, map, "")
        println(map)
    }

    private fun parseProps(props: MutableMap<String, Any>, map: HashMap<String, Any>, prefix: String) {
        for (child in map) {
            if (!(child.value is HashMap<*, *>)) {
                props["${prefix}.${child.key}"] = child.value
            } else {
                val value = child.value as HashMap<String, Any>
                var newPrefix = ""
                newPrefix = if (prefix.isNotEmpty()) {
                    "${prefix}.${child.key}"
                } else {
                    child.key
                }
                parseProps(props, value, newPrefix)
            }
        }


    }


}