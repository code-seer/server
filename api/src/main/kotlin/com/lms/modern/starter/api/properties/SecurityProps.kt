package com.lms.modern.starter.api.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("security")
class SecurityProps {
    var allowCredentials: Boolean = false
    lateinit var allowedOrigins: MutableList<String>
    lateinit var allowedHeaders: MutableList<String>
    lateinit var exposedHeaders: MutableList<String>
    lateinit var allowedMethods: MutableList<String>
    lateinit var allowedPublicApis: MutableList<String>
}