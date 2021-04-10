package io.learnet.data


import io.learnet.util.UtilConfiguration
import io.learnet.util.properties.DataSourceProps
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
@ComponentScan
@EntityScan
@EnableJpaRepositories
@EnableTransactionManagement
@Import(value = [UtilConfiguration::class])
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
