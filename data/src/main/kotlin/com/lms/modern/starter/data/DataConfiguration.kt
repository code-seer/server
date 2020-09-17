package com.lms.modern.starter.data


import com.lms.modern.course.config.SystemConfigConfiguration
import com.lms.modern.course.config.api.SystemConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource


@Configuration
@EntityScan
@ComponentScan
@EnableJpaRepositories
@EnableTransactionManagement
@Import(value = [
    SystemConfigConfiguration::class
])
open class DataConfiguration {

    @Autowired
    lateinit var systemConfig: SystemConfig

    @Bean
    open fun datasource(): DataSource {
        return DataSourceBuilder.create()
                .driverClassName(systemConfig.driverClassName)
                .url(systemConfig.datasourceUrl)
                .username(systemConfig.datasourceUsername)
                .password(systemConfig.datasourcePassword)
                .build()
    }
}