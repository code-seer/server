package com.lms.modern.starter.data.migration

import com.lms.modern.starter.data.DataConfiguration
import com.lms.modern.starter.util.lib.line
import org.elasticsearch.action.admin.indices.cache.clear.ClearIndicesCacheRequest
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.client.indices.GetIndexRequest
import org.flywaydb.core.Flyway
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component


/**
 * Migrate schema and performs bulk indexing for Elasticsearch.
 */
@Component
class FlywayMigrationImpl(
        private val dataConfig: DataConfiguration,
        private val client: RestHighLevelClient
): FlywayMigration {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)
    private val maxNameLength = 60

    private val flyway = Flyway
            .configure()
            .schemas(dataConfig.schemas)
            .validateOnMigrate(true)
            .baselineOnMigrate(true)
            .dataSource(dataConfig.datasourceUrl,
                    dataConfig.datasourceUsername,
                    dataConfig.datasourcePassword)

    override fun init() {
        log.info("  +$line")
        log.info("  | Flyway Schema Migration")
        log.info("  +$line")
        flyway.locations(dataConfig.flywaySchemaLocation).load().migrate()
    }

    override fun clean() {
        if (dataConfig.profile == "dev" || dataConfig.applicationName == "starter") {
            log.info(String.format("  | %-" + maxNameLength.toString() + "s : %s", "Dropping database", "true"))
            Flyway(flyway).clean()
            deleteIndex()
        } else {
            log.info(String.format("  | %-" + maxNameLength.toString() + "s : %s", "Dropping database", "false"))
        }
    }

    override fun deleteIndex() {
        log.info("  +$line")
        log.info("  | Elasticsearch Index Deletion")
        log.info("  +$line")
        val indices: Array<String> = client.indices().get(GetIndexRequest("*"), RequestOptions.DEFAULT).indices
        indices.sort()
        val deletedIndices: MutableList<String> = ArrayList()
        for (index in indices) {
            if (index.startsWith("${dataConfig.indexPrefix}-")) {
                val clearCacheRequest = ClearIndicesCacheRequest(index)
                val deleteRequest = DeleteIndexRequest(index)
                client.indices().clearCache(clearCacheRequest, RequestOptions.DEFAULT)
                val ack = client.indices().delete(deleteRequest, RequestOptions.DEFAULT)
                if (ack.isAcknowledged) {
                    log.info(String.format("  | %-" + maxNameLength.toString() + "s : %s", index, "deleted"))
                    deletedIndices.add(index)
                }
            }
        }
        log.info(String.format("  | %-" + maxNameLength.toString() + "s : %s", "Num deleted", deletedIndices.size))
    }

}