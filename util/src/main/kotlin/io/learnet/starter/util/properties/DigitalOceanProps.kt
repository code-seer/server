package io.learnet.starter.util.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("digitalOcean")
class DigitalOceanProps {
    lateinit var endpoint: String
    lateinit var url: String
    lateinit var key: String
    lateinit var secret: String
}
