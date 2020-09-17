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
}