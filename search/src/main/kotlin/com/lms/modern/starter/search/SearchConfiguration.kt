package com.lms.modern.starter.search

import com.lms.modern.starter.data.DataConfiguration
import com.lms.modern.starter.util.UtilConfiguration
import com.lms.modern.starter.util.properties.SearchProps
import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestHighLevelClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import


@Configuration
@ComponentScan
@Import(value = [
    DataConfiguration::class,
    UtilConfiguration::class])
@EnableConfigurationProperties(SearchProps::class)
class SearchConfiguration {

    @Autowired
    private lateinit var searchProps: SearchProps

    @Bean(destroyMethod = "close")
    fun client(): RestHighLevelClient {
        return RestHighLevelClient(
                RestClient.builder(HttpHost(searchProps.host, searchProps.port)))
    }
}