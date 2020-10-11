package io.learnet.starter.service.api.demoUser

import com.fasterxml.jackson.databind.ObjectMapper
import io.learnet.starter.model.DemoUserDto
import io.learnet.starter.model.PageableRequest
import io.learnet.starter.search.api.LmsPage
import io.learnet.starter.search.api.SearchApi
import io.learnet.starter.service.api.util.sortBuilder
import io.learnet.starter.util.properties.DemoUserProps
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class DemoUserService(private val searchApi: SearchApi,
                      @Qualifier("jacksonObjectMapper")
                      private val objectMapper: ObjectMapper) {

    @Autowired
    lateinit var demoUserProps: DemoUserProps

    fun findAllUsers(pageableRequest: PageableRequest): LmsPage<DemoUserDto> {
        val searchRequest = SearchRequest(demoUserProps.esIndex)
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
