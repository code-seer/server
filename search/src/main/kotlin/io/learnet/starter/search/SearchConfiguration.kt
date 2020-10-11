package io.learnet.starter.search

import io.learnet.starter.data.DataConfiguration
import io.learnet.starter.util.UtilConfiguration
import io.learnet.starter.util.properties.SearchProps
import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestHighLevelClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import


@Configuration
@ComponentScan
@Import(value = [
    DataConfiguration::class,
    UtilConfiguration::class])
class SearchConfiguration {

    @Autowired
    private lateinit var searchProps: SearchProps

    @Bean(destroyMethod = "close")
    fun client(): RestHighLevelClient {
        return RestHighLevelClient(
                RestClient.builder(HttpHost(searchProps.host, searchProps.port)))
    }
}
