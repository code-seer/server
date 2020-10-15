package io.learnet.account.service

import io.learnet.account.search.SearchConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@ComponentScan
@Import(value = [
    SearchConfiguration::class
])
class ServiceConfiguration
