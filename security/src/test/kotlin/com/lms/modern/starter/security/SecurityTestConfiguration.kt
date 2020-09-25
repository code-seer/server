package com.lms.modern.starter.security

import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Import


@SpringBootConfiguration
@EnableAutoConfiguration
@Import(value = [SecurityConfiguration::class])
class SecurityTestConfiguration