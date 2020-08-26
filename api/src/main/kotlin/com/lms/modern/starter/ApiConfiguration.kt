//package com.lms.modern.starter
//
//import com.lms.modern.course.config.api.SystemConfig
//import com.lms.modern.course.data.migration.FlywayMigration
//import com.lms.modern.course.kafka.api.client.KafkaConnectApi
//import com.lms.modern.course.search.SearchConfiguration
//import com.lms.modern.course.search.api.SearchApi
//import com.lms.modern.course.util.lib.line
//import org.slf4j.Logger
//import org.slf4j.LoggerFactory
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.context.event.ApplicationStartedEvent
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.ComponentScan
//import org.springframework.context.annotation.Configuration
//import org.springframework.context.annotation.Import
//import org.springframework.context.event.EventListener
//import org.springframework.web.cors.CorsConfiguration
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource
//import org.springframework.web.filter.CorsFilter
//import org.springframework.web.servlet.config.annotation.CorsRegistry
//import org.springframework.web.servlet.config.annotation.EnableWebMvc
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
//
//
///**
// * Configures internal and external services after the application has started.
// *
// * Kafka Connect, Elasticsearch and Flyway migration needs to be configured once.
// * In a distributed environment, many duplicate applications will be deployed, e.g.
// * in a Kubernetes cluster. In such cases it's dangerous to re-seed the database or
// * re-index Elasticsearch. We will need to alert all running services not to
// * re-configure the services using a Kafka-persisted environment variable. Every newly
// * spawned application needs to check to see if the variable is set before proceeding.
// */
//@Configuration
//@EnableWebMvc
//@ComponentScan
//@Import(value = [ SearchConfiguration::class ] )
//class ApiConfiguration {
//
//    private val log: Logger = LoggerFactory.getLogger(this.javaClass)
//
//    @Autowired
//    lateinit var flywayMigration: FlywayMigration
//
//    @Autowired
//    lateinit var systemConfig: SystemConfig
//
//    @Autowired
//    lateinit var kafkaConnectApi: KafkaConnectApi
//
//    @Autowired
//    lateinit var searchApi: SearchApi
//
//    @Bean
//    fun corsFilter(): CorsFilter {
//        val source = UrlBasedCorsConfigurationSource()
//        val config = CorsConfiguration()
//        val clientUrl = "http://localhost:4100"
//        config.allowCredentials = true
//        config.addAllowedOrigin(clientUrl)
//        config.addAllowedHeader("*")
//        config.addAllowedMethod("GET")
//        config.addAllowedMethod("PUT")
//        config.addAllowedMethod("POST")
//        config.addAllowedMethod("DELETE")
//        config.addAllowedMethod("HEAD")
//        source.registerCorsConfiguration("/**", config)
//        return CorsFilter(source)
//    }
//
////    @EventListener
//    fun onApplicationStart(event: ApplicationStartedEvent) {
//        log.info("  +$line")
//        log.info("  | Configuring Course Service...")
//        log.info("  +$line")
//
//        // TODO: check Kafka to see if flag is set before initializing the events below
//
//        flywayMigration.clean()
//        searchApi.clean()
//        kafkaConnectApi.clean()
//        kafkaConnectApi.init()
//        flywayMigration.init()
//
//        val wait = 30L
//        log.info("Going to sleep for $wait seconds until Elasticsearch finishes indexing")
//        Thread.sleep(wait*1000L)
//    }
//}