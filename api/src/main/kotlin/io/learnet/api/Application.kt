package io.learnet.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory
import org.springframework.context.annotation.Bean

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

@Bean
fun webServerFactoryCustomizer(): WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {
    return WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> { factory -> factory.setContextPath(
            System.getenv("CONTEXT_PATH") as String) }
}
