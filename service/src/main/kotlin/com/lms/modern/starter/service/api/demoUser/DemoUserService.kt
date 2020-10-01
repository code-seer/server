package com.lms.modern.starter.service.api.demoUser

import com.fasterxml.jackson.databind.ObjectMapper
import com.lms.modern.starter.model.DemoUserDto
import com.lms.modern.starter.model.PageableRequest
import com.lms.modern.starter.search.api.LmsPage
import com.lms.modern.starter.search.api.SearchApi
import com.lms.modern.starter.service.api.util.sortBuilder
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class DemoUserService(private val searchApi: SearchApi,
                      @Qualifier("jacksonObjectMapper")
                      private val objectMapper: ObjectMapper) {
    private val demoIndex = "lms-demo_user-read"

    fun findAllUsers(pageableRequest: PageableRequest): LmsPage<DemoUserDto> {
        val searchRequest = SearchRequest(demoIndex)
        val sourceBuilder =  SearchSourceBuilder()
        sourceBuilder
                .size(pageableRequest.limit)
                .from(pageableRequest.offset)
        sourceBuilder.query(QueryBuilders.matchAllQuery())
        sortBuilder(sourceBuilder, pageableRequest)
        searchRequest.source(sourceBuilder)
        return LmsPage(searchApi.execute(searchRequest),
                pageableRequest.offset, pageableRequest.limit, DemoUserDto::class.java, objectMapper)
    }
}