package com.lms.modern.starter.api.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("demouser")
class DemoUserProps {
    lateinit var displayName: String
    lateinit var email: String
    lateinit var password: String
    lateinit var phoneNumber: String
    lateinit var photoUrl: String
}