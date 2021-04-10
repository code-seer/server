package io.learnet.service

import io.learnet.account.data.DataConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import


@Configuration
@ComponentScan
@Import(value = [
    DataConfiguration::class
])
class ServiceConfiguration
