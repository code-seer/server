package io.learnet.api

import io.learnet.api.ApiConfiguration
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Import


@SpringBootConfiguration
@EnableAutoConfiguration
@Import(value = [ApiConfiguration::class])
class ApiTestConfiguration
