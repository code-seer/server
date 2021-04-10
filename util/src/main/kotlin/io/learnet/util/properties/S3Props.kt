package io.learnet.util.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("s3")
class S3Props {
    lateinit var key: String
    lateinit var secret: String
    lateinit var user: String
    lateinit var bucket: String
}