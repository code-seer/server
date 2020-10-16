package io.learnet.account.search.api


import com.fasterxml.jackson.databind.ObjectMapper
import io.learnet.account.data.migration.FlywayMigration
import io.learnet.account.model.DemoUserDto
import io.learnet.account.search.SearchTestConfiguration
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.client.core.CountRequest
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.elasticsearch.search.sort.SortOrder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.annotations.*
import java.lang.reflect.Method
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


/**
 * Tests SearchApi for Elasticsearch integration.
 *
 * Elasticsearch is our source of all reads and full text search operations. Data is always
 * written to our primary Postgres database and synchronized with Elasticsearch. Our primary
 * data is relational, whereas Elasticsearch data is not. The purpose of these tests is to
 * ensure that we can perform forward and inverse lookups that we would have been able to
 * do so easily in SQL.
 *
 */
@SpringBootTest(classes = [SearchTestConfiguration::class])
class SearchApiIntegrationTest: AbstractTestNGSpringContextTests() {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    lateinit var flywayMigration: FlywayMigration

    @Autowired
    lateinit var searchApi: SearchApi

    @Autowired
    @Qualifier("jacksonObjectMapper")
    lateinit var objectMapper: ObjectMapper

    private val testIndex = "learnet-demo_user-read"

    /**
     * Re-index Elasticsearch and sync with Postgres
     */
    @BeforeClass
    fun beforeClass() {
        flywayMigration.init()
    }

    @BeforeMethod
    fun beforeMethod(method: Method) {
        log.info("Testing ${method.name}")
    }

    @Test
    fun demo_user_count_test() {
        val countRequest = CountRequest(arrayOf(testIndex), QueryBuilders.matchAllQuery())
        val response = searchApi.count(countRequest)
        assertEquals( 100, response.count)
    }

    @Test
    fun find_by_id_test() {
        val searchRequest = SearchRequest(testIndex)
        val sourceBuilder =  SearchSourceBuilder()
        sourceBuilder.query(QueryBuilders.termQuery("id", 23))
        searchRequest.source(sourceBuilder)
        val page = LmsPage(searchApi.execute(searchRequest), 0, 1, DemoUserDto::class.java, objectMapper)
        val firstName = "Raymonde"
        val avatar = "https://s3.amazonaws.com/uifaces/faces/twitter/ajaxy_ru/128.jpg"
        val address = "West Matthewchester, OR"
        assertNotNull(page)
        assertEquals(1, page.totalRecords)
        assertEquals(1, page.numPages)
        assertEquals(firstName, page.records[0].firstName)
        assertEquals(avatar, page.records[0].avatar)
        assertEquals(true, page.records[0].address.contains(address))
    }

    @Test
    fun find_all_sort_test() {
        val searchRequest = SearchRequest(testIndex)
        val sourceBuilder =  SearchSourceBuilder()
        sourceBuilder.query(QueryBuilders.matchAllQuery())
        val offset = 0
        val size = 25
        sourceBuilder
                .sort("id", SortOrder.ASC)
                .size(size)
                .from(offset)
                .query(QueryBuilders.matchAllQuery())
        searchRequest.source(sourceBuilder)
        val page = LmsPage(searchApi.execute(searchRequest), offset, size, DemoUserDto::class.java, objectMapper)
        assertNotNull(page)
        assertEquals(100, page.totalRecords)
        assertEquals( 4, page.numPages)
        assertEquals( "Roxann", page.records[6].firstName)
        assertEquals( "Walter", page.records[6].lastName)
    }
}
