package com.lms.modern.starter.util

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.lms.modern.starter.util.lib.CustomObjectMapper
import com.lms.modern.starter.util.lib.OffsetDateTimeDeserializer
import com.lms.modern.starter.util.lib.OffsetDateTimeSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.time.OffsetDateTime

@Configuration
@ComponentScan
open class UtilConfiguration {

    @Bean
    @Primary
    open fun customObjectMapper(): CustomObjectMapper {
        val mapper = CustomObjectMapper()
        val javaTimeModule = JavaTimeModule()
        javaTimeModule.addSerializer(OffsetDateTime::class.java, OffsetDateTimeSerializer())
        javaTimeModule.addDeserializer(OffsetDateTime::class.java, OffsetDateTimeDeserializer())
        mapper.o.registerModule(javaTimeModule)
        return mapper
    }
}