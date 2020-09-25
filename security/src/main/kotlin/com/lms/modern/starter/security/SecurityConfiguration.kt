package com.lms.modern.starter.security

import com.lms.modern.starter.config.SystemConfigConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@ComponentScan
@Import(value = [SystemConfigConfiguration::class])
class SecurityConfiguration {
}