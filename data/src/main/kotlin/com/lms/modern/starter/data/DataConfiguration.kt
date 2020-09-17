package com.lms.modern.starter.data


import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

@Configuration
@EntityScan
@ComponentScan
@EnableJpaRepositories
@EnableTransactionManagement
open class DataConfiguration {

    @Value("\${spring.datasource.driver-class-name: ---Driver class name not found---}")
    var driverClassName = ""

    @Value("\${spring.datasource.url: ---Datasource url not found---}")
    var datasourceUrl = ""

    @Value("\${spring.datasource.username: ---Datasource username not found---}")
    var datasourceUsername = ""

    @Value("\${spring.datasource.password: ---Datasource password not found---}")
    var datasourcePassword = ""

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