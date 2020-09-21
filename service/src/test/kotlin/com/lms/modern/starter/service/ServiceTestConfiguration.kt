package com.lms.modern.starter.service

import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Import

@SpringBootConfiguration
@EnableAutoConfiguration
@Import(value = [ServiceConfiguration::class])
class ServiceTestConfiguration