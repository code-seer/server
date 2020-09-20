//package com.lms.modern.starter.search.api
//
//
////import com.lms.modern.starter.config.api.SystemConfig
////import com.lms.modern.starter.data.migration.FlywayMigration
//////import com.lms.modern.starter.kafka.api.client.KafkaConnectApi
////import com.lms.modern.starter.model.CourseDto
////import com.lms.modern.starter.model.FaqDto
//import com.lms.modern.starter.search.SearchTestConfiguration
//import org.elasticsearch.action.search.SearchRequest
//import org.elasticsearch.client.core.CountRequest
//import org.elasticsearch.index.query.QueryBuilders
//import org.elasticsearch.search.builder.SearchSourceBuilder
//import org.elasticsearch.search.sort.SortOrder
//import org.slf4j.Logger
//import org.slf4j.LoggerFactory
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
//import org.testng.annotations.*
//import java.lang.reflect.Method
//import kotlin.test.assertEquals
//import kotlin.test.assertNotNull
//
//
///**
// * Tests SearchApi for Elasticsearch integration.
// *
// * Elasticsearch is our source f all reads and full text search operations. Data is always
// * written to our primary Postgres database and synchronized with Elasticsearch using Kafka
// * Streams. Our primary data is relational, whereas Elasticsearch data is not relational at
// * all. The purpose of these tests is to ensure that we can perform forward and inverse
// * lookups that we would have been able to do so easily in SQL.
// *
// * TODO: Implement tests that require other services
// *      - findCoursesByEnrollmentCount
// *      - findCoursesByInstructorName
// *      - findCoursesByInstructorUuid
// *
// */
//@SpringBootTest(classes = [SearchTestConfiguration::class])
//class SearchApiIntegrationTest: AbstractTestNGSpringContextTests() {
//
//    private val log: Logger = LoggerFactory.getLogger(this.javaClass)
//
//    @Autowired
//    lateinit var flywayMigration: FlywayMigration
//
//    @Autowired
//    lateinit var systemConfig: SystemConfig
//
//    @Autowired
////    lateinit var kafkaConnectApi: KafkaConnectApi
//
//    @Autowired
//    lateinit var searchApi: SearchApi
//
//    /**
//     * Re-index Elasticsearch and sync with Postgres
//     */
//    @BeforeClass
//    fun beforeClass() {
//        flywayMigration.clean()
//        searchApi.clean()
////        kafkaConnectApi.clean()
////        kafkaConnectApi.init()
//        flywayMigration.init()
//
//        // There's a race condition between Flyway migration and Elasticsearch indexing.
//        // We need to slow down so the indexing can finish before the tests start
//        val wait = 15L
//        log.info("Going to sleep for $wait seconds until Elasticsearch finishes indexing")
//        Thread.sleep(wait*1000L)
//    }
//
//    @AfterClass
//    fun afterClass() {
//        flywayMigration.clean()
//        searchApi.clean()
////        kafkaConnectApi.clean()
//    }
//
//    @BeforeMethod
//    fun beforeMethod(method: Method) {
//        log.info("Testing ${method.name}")
//    }
//
//    @Test
//    fun getCourseCount() {
//        val countRequest = CountRequest(arrayOf(systemConfig.courseIndex), QueryBuilders.matchAllQuery())
//        val response = searchApi.count(countRequest)
//        assertEquals( 606, response.count)
//    }
//
//    @DataProvider
//    fun paginatedCoursesTestCases(): Array<Array<Any>> {
//        return arrayOf(
//                arrayOf(0, 10, "id", SortOrder.ASC, "Gamification", "game elements and digital game design"),
//                arrayOf(10, 10, "id", SortOrder.ASC,
//                        " Fundamentals of the Chinese character writing. Part 2",
//                        "If you just finished the first course, you will definitely love the second one!"),
//                arrayOf(600, 10, "id", SortOrder.ASC, "Spring Boot Test course",
//                        "This is a test course created for testing Spring Data integration")
//        )
//    }
//
//    @Test(dataProvider = "paginatedCoursesTestCases")
//    fun findAllPaginatedCourses(testCase: Array<Any>) {
//        val offset: Int = testCase[0] as Int
//        val size: Int = testCase[1] as Int
//        val sortKey: String = testCase[2] as String
//        val sortDir: SortOrder = testCase[3] as SortOrder
//        val name: String = testCase[4] as String
//        val description: String = testCase[5] as String
//        val searchRequest = SearchRequest(systemConfig.courseIndex)
//        val sourceBuilder =  SearchSourceBuilder()
//        sourceBuilder
//                .sort(sortKey, sortDir)
//                .size(size)
//                .from(offset)
//                .query(QueryBuilders.matchAllQuery())
//        searchRequest.source(sourceBuilder)
//        val page = Page(searchApi.execute(searchRequest), offset, size, CourseDto::class.java)
//        assertNotNull(page)
//        assertEquals(606, page.totalRecords)
//        assertEquals( 61, page.numPages)
//        assertEquals(name, page.records[0].name)
//        assertEquals( true, page.records[0].description.contains(description))
//    }
//
//    @Test
//    fun findCourseById() {
//        val searchRequest = SearchRequest(systemConfig.courseIndex)
//        val sourceBuilder =  SearchSourceBuilder()
//        sourceBuilder.query(QueryBuilders.termQuery("id", 1))
//        searchRequest.source(sourceBuilder)
//        val page = Page(searchApi.execute(searchRequest), 0, 1, CourseDto::class.java)
//        val name = "Gamification"
//        val description = "game elements and digital game design"
//        assertNotNull(page)
//        assertEquals(1, page.totalRecords)
//        assertEquals(1, page.numPages)
//        assertEquals(name, page.records[0].name)
//        assertEquals(true, page.records[0].description.contains(description))
//    }
//
//    @Test
//    fun findCourseByUUid() {
//        val searchRequest = SearchRequest(systemConfig.courseIndex)
//        val sourceBuilder =  SearchSourceBuilder()
//        val uuid = "ba71464b-f21f-4de8-a2cd-1dc2c11f3799"
//        sourceBuilder.query(QueryBuilders.termQuery("_uuid.keyword", uuid))
//        searchRequest.source(sourceBuilder)
//        val page = Page(searchApi.execute(searchRequest), 0, 1, CourseDto::class.java)
//        val name = "Gamification"
//        val description = "game elements and digital game design"
//        assertNotNull(page)
//        assertEquals(1, page.totalRecords)
//        assertEquals(1, page.numPages)
//        assertEquals(name, page.records[0].name)
//        assertEquals(true, page.records[0].description.contains(description))
//    }
//
//    @Test
//    fun findCoursesByCreationDate() {
//        val searchRequest = SearchRequest(systemConfig.courseIndex)
//        val sourceBuilder =  SearchSourceBuilder()
//        val gte = "2030-12-01T00:00:00Z"
//        val lte = "2050-12-01T00:00:00Z"
//        sourceBuilder.query(QueryBuilders
//                .rangeQuery("created_dt.keyword")
//                .from(gte)
//                .to(lte))
//        searchRequest.source(sourceBuilder)
//        val page = Page(searchApi.execute(searchRequest), 0, 10, CourseDto::class.java)
//        assertNotNull(page)
//        assertEquals(337, page.totalRecords)
//        assertEquals(34, page.numPages)
//    }
//
//    @Test
//    fun findCoursesBySubject() {
//        val searchRequest = SearchRequest(systemConfig.courseIndex)
//        val sourceBuilder =  SearchSourceBuilder()
//        val subject = "The Enlightenment,Root.History.Modern_History.The_Enlightenment"
//        sourceBuilder.query(QueryBuilders.termQuery("subject.keyword", subject))
//        searchRequest.source(sourceBuilder)
//        val page = Page(searchApi.execute(searchRequest), 0, 10, CourseDto::class.java)
//        assertNotNull(page)
//        assertEquals(19, page.totalRecords)
//        assertEquals(2, page.numPages)
//    }
//
//    @Test
//    fun findCourseByFullTextSearch() {
//        val searchRequest = SearchRequest(systemConfig.courseIndex)
//        val sourceBuilder =  SearchSourceBuilder()
//        sourceBuilder.query(QueryBuilders.queryStringQuery("global security"))
//        searchRequest.source(sourceBuilder)
//        val page = Page(searchApi.execute(searchRequest), 0, 10, CourseDto::class.java)
//        assertNotNull(page)
//        assertEquals(118, page.totalRecords)
//        assertEquals(12, page.numPages)
//    }
//
//    @DataProvider
//    fun prerequisitesByCourseTestCases(): Array<Array<Any>> {
//        return arrayOf(
//                arrayOf(-1, 0, 10, SortOrder.ASC, 0L, 0L, 999L, 999L),
//                arrayOf(22, 0, 10, SortOrder.ASC, 3L, 1L, 263L, 495L),
//                arrayOf(22, 0, 10, SortOrder.DESC, 3L, 1L, 495L, 263L)
//        )
//    }
//
//    @Test(dataProvider = "prerequisitesByCourseTestCases")
//    fun findPrerequisitesByCourseId(testCase: Array<Any>) {
//        val courseId = testCase[0] as Int
//        val offset = testCase[1] as Int
//        val limit = testCase[2] as Int
//        val sortOrder = testCase[3] as SortOrder
//        val totalRecords = testCase[4] as Long
//        val numPages = testCase[5] as Long
//        val firstId = testCase[6] as Long
//        val lastId = testCase[7] as Long
//        val page = Page(searchApi.join(
//                systemConfig.prerequisiteIndex,
//                systemConfig.courseIndex,
//                "requiring_course_id",
//                courseId,
//                "required_course_id",
//                offset,
//                limit,
//                sortOrder,
//                CourseDto::class.java), offset, limit, CourseDto::class.java)
//        assertNotNull(page)
//        assertEquals(totalRecords, page.totalRecords)
//        assertEquals(numPages, page.numPages)
//        if (totalRecords > 0) {
//            assertEquals(firstId, page.records[0].id)
//            assertEquals(lastId, page.records[2].id)
//        }
//    }
//
//    @Test
//    fun findCoursesByPrerequisiteId() {
//        val page = Page(searchApi.join(
//                systemConfig.prerequisiteIndex,
//                systemConfig.courseIndex,
//                "required_course_id",
//                495,
//                "requiring_course_id",
//                0,
//                10,
//                SortOrder.ASC,
//                CourseDto::class.java), 0, 10, CourseDto::class.java)
//        assertNotNull(page)
//        assertEquals(2, page.totalRecords)
//        assertEquals(22, page.records[0].id)
//        assertEquals("Draft", page.records[0].status)
//        assertEquals("Absolute Beginner", page.records[0].level)
//        assertEquals(193, page.records[1].id)
//        assertEquals("Draft", page.records[1].status)
//        assertEquals("Low", page.records[1].level)
//    }
//
//    @Test
//    fun findFAQsByCourseId() {
//        val searchRequest = SearchRequest(systemConfig.faqIndex)
//        val sourceBuilder =  SearchSourceBuilder()
//        sourceBuilder.query(QueryBuilders.termQuery("course_id", 13))
//        searchRequest.source(sourceBuilder)
//        val page = Page(searchApi.execute(searchRequest), 0, 10, FaqDto::class.java)
//        assertNotNull(page)
//        assertEquals(1, page.totalRecords)
//        assertEquals(231, page.records[0].id)
//        assertEquals("Advanced solitary necessity", page.records[0].question)
//        assertEquals("Delineations harpooneers touch", page.records[0].answer)
//    }
//
//    @Test
//    fun findCourseByFAQId() {
//        val page = Page(searchApi.join(
//                systemConfig.faqIndex,
//                systemConfig.courseIndex,
//                "id",
//                231,
//                "course_id",
//                0,
//                10,
//                SortOrder.ASC,
//                CourseDto::class.java), 0, 10, CourseDto::class.java)
//        assertNotNull(page)
//        assertEquals(1, page.totalRecords)
//        assertEquals(13, page.records[0].id)
//        assertEquals("Absolute Beginner", page.records[0].level)
//        assertEquals("Draft", page.records[0].status)
//        assertEquals("Online", page.records[0].instructionMode)
//    }
//}