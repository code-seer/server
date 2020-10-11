package io.learnet.starter.util.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("spring.datasource")
class DataSourceProps {
    lateinit var driverClassName: String
    lateinit var url: String
    lateinit var username: String
    lateinit var password: String
}
