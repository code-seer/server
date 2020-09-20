//package com.lms.modern.starter.search.api
//
//import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
//import com.lms.modern.starter.config.api.SystemConfig
//import com.lms.modern.starter.util.lib.line
//import org.elasticsearch.action.admin.indices.cache.clear.ClearIndicesCacheRequest
//import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest
//import org.elasticsearch.action.search.SearchRequest
//import org.elasticsearch.action.search.SearchResponse
//import org.elasticsearch.client.RequestOptions
//import org.elasticsearch.client.RestHighLevelClient
//import org.elasticsearch.client.core.CountRequest
//import org.elasticsearch.client.core.CountResponse
//import org.elasticsearch.client.indices.GetIndexRequest
//import org.elasticsearch.index.query.QueryBuilders
//import org.elasticsearch.search.SearchHit
//import org.elasticsearch.search.builder.SearchSourceBuilder
//import org.elasticsearch.search.sort.SortOrder
//import org.slf4j.Logger
//import org.slf4j.LoggerFactory
//import org.springframework.stereotype.Service
//import javax.annotation.PostConstruct
//
//
//@Service
//class SearchApiImpl(
//        private val client: RestHighLevelClient,
//        private val systemConfig: SystemConfig
//) : SearchApi {
//
//    private val log: Logger = LoggerFactory.getLogger(this.javaClass)
//    private val maxNameLength = 60
//
//    override fun clean(): MutableList<String> {
//        log.info("  +$line")
//        log.info("  | Elasticsearch Index Deletion")
//        log.info("  +$line")
//        val indices: Array<String> = client.indices().get(GetIndexRequest("*"), RequestOptions.DEFAULT).indices
//        indices.sort()
//        val deletedIndices: MutableList<String> = ArrayList()
//        for (index in indices) {
//            if (index.startsWith("${systemConfig.kafkaConnectorPrefix}.")) {
//                val clearCacheRequest = ClearIndicesCacheRequest(index)
//                val deleteRequest = DeleteIndexRequest(index)
//                client.indices().clearCache(clearCacheRequest, RequestOptions.DEFAULT)
//                val ack = client.indices().delete(deleteRequest, RequestOptions.DEFAULT)
//                if (ack.isAcknowledged) {
//                    log.info(String.format("  | %-" + maxNameLength.toString() + "s : %s", index, "deleted"))
//                    deletedIndices.add(index)
//                }
//            }
//        }
//        log.info(String.format("  | %-" + maxNameLength.toString() + "s : %s", "Num deleted", deletedIndices.size))
//        return deletedIndices
//    }
//
//    override fun execute(request: SearchRequest): SearchResponse {
//        return client.search(request, RequestOptions.DEFAULT)
//    }
//
//    override fun <C> join(
//            parentIndex: String,
//            childIndex: String,
//            parentKey: String,
//            parentValue: Any,
//            childField: String,
//            offset: Int,
//            limit: Int,
//            sortDir: SortOrder,
//            childType: Class<C>): SearchResponse {
//
//        // Get parent result
//        val searchRequest = SearchRequest(parentIndex)
//        val sourceBuilder =  SearchSourceBuilder()
//        sourceBuilder.query(QueryBuilders.termQuery(parentKey, parentValue))
//                .size(limit)
//                .from(offset)
//                .sort(childField, sortDir)
//        searchRequest.source(sourceBuilder)
//        val response = execute(searchRequest)
//
//        // Get child results
//        val childIds = response.hits.map { h -> jacksonObjectMapper().readTree(h.sourceAsString).get(childField).asInt() }
//        val childSearchRequest = SearchRequest(childIndex)
//        val childSourceBuilder = SearchSourceBuilder()
//        childSourceBuilder.query(QueryBuilders.termsQuery("id", childIds)).sort("id", sortDir)
//        childSearchRequest.source(childSourceBuilder)
//        return execute(childSearchRequest)
//    }
//
//    override fun count(request: CountRequest): CountResponse {
//        return client.count(request, RequestOptions.DEFAULT)
//    }
//}
