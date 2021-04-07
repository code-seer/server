package io.learnet.account.data.migration

interface FlywayMigration {
    /**
     * Initialize development/production database. If the application is in
     * development mode, drop the default schema and create a new one. After
     * migrating the schema, mass index all tables.
     */
    fun init()

    /**
     * Drop database and delete search indices. Not allowed in production.
     */
    fun clean()

    /**
     * Index existing data to Elasticsearch.
     */
//    fun indexData()

}
