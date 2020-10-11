package io.learnet.starter.service

import io.learnet.starter.search.SearchConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@ComponentScan
@Import(value = [
    SearchConfiguration::class
])
class ServiceConfiguration
