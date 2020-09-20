//package com.lms.modern.starter.search
//
//import com.lms.modern.starter.config.SystemConfigConfiguration
//import com.lms.modern.starter.config.api.SystemConfig
//import com.lms.modern.starter.data.DataConfiguration
//import com.lms.modern.starter.kafka.KafkaConfiguration
//import com.lms.modern.starter.util.UtilConfiguration
//import org.apache.http.HttpHost
//import org.elasticsearch.client.RestClient
//import org.elasticsearch.client.RestHighLevelClient
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.ComponentScan
//import org.springframework.context.annotation.Configuration
//import org.springframework.context.annotation.Import
//
//
//@Configuration
//@ComponentScan
//@Import(value = [
//    DataConfiguration::class,
//    SystemConfigConfiguration::class,
//    KafkaConfiguration::class,
//    UtilConfiguration::class])
//class SearchConfiguration {
//
//    @Autowired
//    lateinit var config: SystemConfig
//
//    @Bean(destroyMethod = "close")
//    fun client(): RestHighLevelClient {
//        return RestHighLevelClient(
//                RestClient.builder(HttpHost(config.elasticsearchHost, config.elasticsearchPort)))
//    }
//}