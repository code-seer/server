package com.lms.modern.starter.search.api

import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.client.core.CountRequest
import org.elasticsearch.client.core.CountResponse
import org.elasticsearch.search.sort.SortOrder

/**
 * Implements a high-level API on top of Elasticsearch REST API.
 */
interface SearchApi {

    /**
     * Initialize Elasticsearch by dropping any existing indices
     * belonging to Course Service.
     */
    fun clean(): MutableList<String>

    /**
     * Executes an Elasticsearch search query and returns a general
     * SearchResponse object.
     *
     * A search query can be any type of supported Elasticsearch
     * query. It is up to the caller to build the query before calling
     * this method.
     */
    fun execute(request: SearchRequest): SearchResponse

    /**
     * Performs an application-side join.
     *
     * Our primary data is inherently relational and stored in Postgres.
     * By using Elasticsearch as our source for reads, we lose that relational
     * aspect that we get for free with SQL. As a result, our data is usually
     * highly denormalized but sometimes it's just not possible to eliminate
     * all relationships. This is specially true for many-to-many relationships.
     * Although there are other ways that people deal with this sort of problem,
     * we've opted for a simpler approach that only costs us two round-trip
     * queries. This is also an approach recommended by Elasticsearch.
     *
     * In order to perform an application side join, we first retrieve the
     * lookup values of all the children. And then we issue a terms query
     * with an array of the lookup values. Elasticsearch then consolidates
     * the results into a single response object before sending it back to
     * us. The second of the join queries can be extended to perform a multi-get
     * request to search across multiple indices.
     *
     */
    fun <C> join(
            parentIndex: String,
            childIndex: String,
            parentKey: String,
            parentValue: Any,
            childField: String,
            offset: Int,
            limit: Int,
            sortDir: SortOrder,
            childType: Class<C>): SearchResponse

    /**
     * Gets a count of the query specified in CountRequest.
     *
     * Sometimes we're only interested in the count of records and
     * it is more efficient to leverage Elasticsearch's count request
     * than to perform a general search request.
     */
    fun count(request: CountRequest): CountResponse
}