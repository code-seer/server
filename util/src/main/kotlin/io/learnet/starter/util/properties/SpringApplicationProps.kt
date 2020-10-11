package io.learnet.starter.util.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("spring.application")
class SpringApplicationProps {
    lateinit var name: String
}
