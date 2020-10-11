package io.learnet.starter.util.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("spring.flyway")
class FlywayProps {
    lateinit var schemas: String
    lateinit var locations: String
}
