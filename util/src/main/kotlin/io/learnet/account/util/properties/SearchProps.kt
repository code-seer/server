package io.learnet.account.util.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("elasticsearch")
class SearchProps {
    lateinit var host: String
    var port: Int = 9200
    lateinit var username: String
    lateinit var password: String
    lateinit var scheme: String
}
