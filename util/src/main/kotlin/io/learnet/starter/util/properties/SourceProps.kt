package io.learnet.starter.util.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("source")
class SourceProps {
    lateinit var profile: String
    lateinit var name: String
}
