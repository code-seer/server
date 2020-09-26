package com.lms.modern.starter.service.api.util

import com.lms.modern.starter.model.PageableRequest
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.elasticsearch.search.sort.SortOrder

private fun sortOrder(s: String): SortOrder {
    if (s.toLowerCase() == "asc") { return SortOrder.ASC }
    return SortOrder.DESC
}

fun sortBuilder(builder: SearchSourceBuilder, request: PageableRequest) {
    if (request.sortBy != null && request.sortDir != null) {
        assert(request.sortDir.size == request.sortBy.size)
        for (i in 0 until request.sortBy.size) {
            val field = if (request.sortBy[i] == "id") "id" else "${request.sortBy[i]}_raw"
            builder.sort(field, sortOrder(request.sortDir[i]))
        }
    }
}