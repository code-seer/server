package com.lms.modern.starter.search.api

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.client.core.CountRequest
import org.elasticsearch.client.core.CountResponse
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.elasticsearch.search.sort.SortOrder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service


@Service
class SearchApiImpl(private val client: RestHighLevelClient) : SearchApi {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)
    private val maxNameLength = 60

    override fun execute(request: SearchRequest): SearchResponse {
        return client.search(request, RequestOptions.DEFAULT)
    }

    override fun <C> join(
            parentIndex: String,
            childIndex: String,
            parentKey: String,
            parentValue: Any,
            childField: String,
            offset: Int,
            limit: Int,
            sortDir: SortOrder,
            childType: Class<C>): SearchResponse {

        // Get parent result
        val searchRequest = SearchRequest(parentIndex)
        val sourceBuilder =  SearchSourceBuilder()
        sourceBuilder.query(QueryBuilders.termQuery(parentKey, parentValue))
                .size(limit)
                .from(offset)
                .sort(childField, sortDir)
        searchRequest.source(sourceBuilder)
        val response = execute(searchRequest)

        // Get child results
        val childIds = response.hits.map { h -> jacksonObjectMapper().readTree(h.sourceAsString).get(childField).asInt() }
        val childSearchRequest = SearchRequest(childIndex)
        val childSourceBuilder = SearchSourceBuilder()
        childSourceBuilder.query(QueryBuilders.termsQuery("id", childIds)).sort("id", sortDir)
        childSearchRequest.source(childSourceBuilder)
        return execute(childSearchRequest)
    }

    override fun count(request: CountRequest): CountResponse {
        return client.count(request, RequestOptions.DEFAULT)
    }
}
