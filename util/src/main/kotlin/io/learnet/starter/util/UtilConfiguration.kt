package io.learnet.starter.util

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.learnet.starter.util.lib.OffsetDateTimeDeserializer
import io.learnet.starter.util.lib.OffsetDateTimeSerializer
import io.learnet.starter.util.properties.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import java.time.OffsetDateTime

@Configuration
@ComponentScan
@EnableConfigurationProperties(
        SecurityProps::class,
        FirebaseProps::class,
        DemoUserProps::class,
        DataSourceProps::class,
        FlywayProps::class,
        SearchProps::class,
        SourceProps::class
)
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
