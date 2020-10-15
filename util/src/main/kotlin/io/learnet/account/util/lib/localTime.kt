package io.learnet.account.util.lib

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

/**
 * Take a local time with time zone information and return a time without time zone.
 * For example, a server running in Boston should return 09:30:00 for an input value of
 * 14:30:00Z. The Default server offset for Boston is UTC-5.
 *
 * TODO: The above values were recorded on 6/26/20 in Cambridge, MA while DST is active. T
 *      he UTC format does not reflect the database value. Where is the bug located? Debezium?
 *      Elasticsearch?
 */
fun utcToLocalTime(localTimeWithZone: String?): String? {
    if (localTimeWithZone != null) {
        val localTimePattern = "HH:mm:ssZ"
        val dateTimeFormatter = DateTimeFormat.forPattern(localTimePattern)
        val localTime: DateTime = dateTimeFormatter.parseDateTime(localTimeWithZone)
        return localTime.toLocalTime().toString(localTimePattern)
    }
    return null
}
