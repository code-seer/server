package com.lms.modern.starter.util

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.lms.modern.starter.util.lib.OffsetDateTimeDeserializer
import com.lms.modern.starter.util.lib.OffsetDateTimeSerializer
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import java.time.OffsetDateTime

@Configuration
@ComponentScan
class UtilConfiguration {

    @Bean
    @Qualifier("jacksonObjectMapper")
    fun objectMapper(): ObjectMapper {
        val objectMapper = jacksonObjectMapper()
        val javaTimeModule = JavaTimeModule()
        javaTimeModule.addSerializer(OffsetDateTime::class.java, OffsetDateTimeSerializer())
        javaTimeModule.addDeserializer(OffsetDateTime::class.java, OffsetDateTimeDeserializer())
        objectMapper.registerModule(javaTimeModule)
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        return objectMapper
    }
}