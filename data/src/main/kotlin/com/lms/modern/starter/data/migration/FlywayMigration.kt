package com.lms.modern.starter.data.migration

interface FlywayMigration {
    /**
     * Initialize development/production database. If the application is in
     * development mode, drop the default schema and create a new one.
     */
    fun init()

    /**
     * Drop database. Not allowed in production.
     */
    fun clean()

    /**
     * Delete all Elasticserch indices.
     */
    fun deleteIndex()

    /**
     * Bulk index new migration for Elasticsearch.
     */
//    fun index()
//    private val threadCount = 5
//    override fun index() {
//        log.info("Bulk indexing")
//        val searchSession = Search.session(entityManager)
//        val indexer: MassIndexer = searchSession.massIndexer(DemoUserEntity::class.java)
//                .threadsToLoadObjects(threadCount)
//        indexer.startAndWait()
//    }
}