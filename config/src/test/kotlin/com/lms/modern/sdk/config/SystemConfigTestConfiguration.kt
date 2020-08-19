package com.lms.modern.sdk.config

import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Import

@SpringBootConfiguration
@EnableAutoConfiguration
@Import(value = [SystemConfigConfiguration::class])
open class SystemConfigTestConfiguration