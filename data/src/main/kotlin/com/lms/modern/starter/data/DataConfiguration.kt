package com.lms.modern.starter.data


import com.lms.modern.starter.config.SystemConfigConfiguration
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

@Configuration
@ComponentScan
@EntityScan
@EnableJpaRepositories
@EnableTransactionManagement
@Import(value = [SystemConfigConfiguration::class])
//@EnableConfigurationProperties()
open class DataConfiguration(
        @Value("\${spring.datasource.driver-class-name: ---Driver class name not found---}")
        open val driverClassName: String,

        @Value("\${spring.datasource.url: ---Datasource url not found---}")
        open var datasourceUrl: String,

        @Value("\${spring.datasource.username: ---Datasource username not found---}")
        open var datasourceUsername: String,

        @Value("\${spring.datasource.password: ---Datasource password not found---}")
        open var datasourcePassword: String,

        @Value("\${spring.flyway.schemas: ---Database schemas not found---}")
        open var schemas: String,

        @Value("\${spring.flyway.locations: ---spring.flyway.locations not set---}")
        open var flywaySchemaLocation: String,

        @Value("\${source.profile: ---Profile name not found---}")
        open var profile: String,

        @Value("\${spring.application.name: ---Application name not found---}")
        open var applicationName: String
) {

    @Bean
    open fun datasource(): DataSource {
        return DataSourceBuilder.create()
                .driverClassName(driverClassName)
                .url(datasourceUrl)
                .username(datasourceUsername)
                .password(datasourcePassword)
                .build()
    }
}