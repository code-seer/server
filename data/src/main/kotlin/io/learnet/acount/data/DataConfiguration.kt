package io.learnet.acount.data


import io.learnet.account.config.SystemConfigConfiguration
import io.learnet.account.util.properties.DataSourceProps
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
@ComponentScan
@EntityScan
@EnableJpaRepositories
@EnableTransactionManagement
@Import(value = [SystemConfigConfiguration::class])
open class DataConfiguration {

    @Autowired
    lateinit var dataSourceProps: DataSourceProps

    @Bean
    open fun datasource(): DataSource {
        return DataSourceBuilder.create()
                .driverClassName(dataSourceProps.driverClassName)
                .url(dataSourceProps.url)
                .username(dataSourceProps.username)
                .password(dataSourceProps.password)
                .build()
    }
}
