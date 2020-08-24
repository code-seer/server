package com.lms.modern.starter.config

import com.lms.modern.starter.util.UtilConfiguration
import org.springframework.context.annotation.*

@Configuration
@ComponentScan
@Import(value = [UtilConfiguration::class])
open class SystemConfigConfiguration

