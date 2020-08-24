package com.lms.modern.starter.util.lib

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME

val FORMATTER: DateTimeFormatter = ISO_OFFSET_DATE_TIME

class OffsetDateTimeSerializer: JsonSerializer<OffsetDateTime>() {

    override fun serialize(p0: OffsetDateTime?, p1: JsonGenerator?, p2: SerializerProvider?) {
       p1?.writeString(p0?.format(FORMATTER))
    }
}

class OffsetDateTimeDeserializer: JsonDeserializer<OffsetDateTime>() {
    override fun deserialize(p0: JsonParser?, p1: DeserializationContext?): OffsetDateTime {
        return OffsetDateTime.parse(p0?.valueAsString, FORMATTER)
    }
}
