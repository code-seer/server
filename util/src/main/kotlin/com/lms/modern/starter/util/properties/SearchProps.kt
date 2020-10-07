package com.lms.modern.starter.util.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("elasticsearch")
class SearchProps {
    lateinit var host: String
    var port: Int = 9200
}