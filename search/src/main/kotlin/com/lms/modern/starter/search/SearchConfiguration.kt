package com.lms.modern.starter.search

import com.lms.modern.starter.data.DataConfiguration
import com.lms.modern.starter.util.UtilConfiguration
import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestHighLevelClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import


@Configuration
@ComponentScan
@Import(value = [
    DataConfiguration::class,
    UtilConfiguration::class])
class SearchConfiguration(
    @Value("\${elasticsearch.host: ---Elasticsearch host not found---}")
    open var elasticsearchHost: String,

    @Value("\${elasticsearch.port: 9200}")
    open var elasticsearchPort: Int
) {

    @Bean(destroyMethod = "close")
    open fun client(): RestHighLevelClient {
        return RestHighLevelClient(
                RestClient.builder(HttpHost(elasticsearchHost, elasticsearchPort)))
    }
}