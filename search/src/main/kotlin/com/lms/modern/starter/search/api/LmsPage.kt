package com.lms.modern.starter.search.api

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.lms.modern.starter.util.lib.OffsetDateTimeDeserializer
import com.lms.modern.starter.util.lib.OffsetDateTimeSerializer
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.search.SearchHit
import java.time.OffsetDateTime
import kotlin.collections.ArrayList
import kotlin.math.ceil

/**
 * Takes a standard Elasticsearch SearchResponse object and unpacks it to provide
 * a convenient abstraction over the underlying response object.
 */
class LmsPage<T>(searchResponse: SearchResponse,
                 offset: Int,
                 limit: Int,
                 type: Class<T>
){
    private val objectMapper = jacksonObjectMapper()
    var limit: Int = 0
    var offset: Int = 0
    var totalRecords: Long = 0
    var numPages: Long = 0
    var records: MutableList<T> = ArrayList()

    init {
        addModule()
        if (searchResponse.hits.totalHits?.value!! > 0L) {
            this.totalRecords = searchResponse.hits.totalHits!!.value
            this.limit = limit
            this.offset = offset
            this.numPages = ceil((this.totalRecords.toDouble()/limit.toDouble())).toLong()
            for (hit: SearchHit in searchResponse.hits) {
                this.records.add(objectMapper.readValue(hit.sourceAsString, type) as T)
            }
        }
    }

    private fun addModule() {
        val javaTimeModule = JavaTimeModule()
        javaTimeModule.addSerializer(OffsetDateTime::class.java, OffsetDateTimeSerializer())
        javaTimeModule.addDeserializer(OffsetDateTime::class.java, OffsetDateTimeDeserializer())
        objectMapper.registerModule(javaTimeModule)
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }
}