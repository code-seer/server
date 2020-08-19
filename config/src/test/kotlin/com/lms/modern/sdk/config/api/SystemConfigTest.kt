package com.lms.modern.sdk.config.api


import com.lms.modern.sdk.config.SystemConfigTestConfiguration
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.annotations.*
import java.lang.reflect.Method
import java.time.OffsetDateTime
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Tests course creation through JPA and retrieval of created objects from
 * Elasticsearch index. Applies a delay for index synchronization.
 */
@SpringBootTest(classes = [SystemConfigTestConfiguration::class])
class SystemConfigTest: AbstractTestNGSpringContextTests() {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    @Test
    fun funTest() {
        log.info("this is a fun test!")
    }

//    @Autowired
//    lateinit var courseService: CourseService

//    @Autowired
//    lateinit var flywayMigration: FlywayMigration
//
//    @Autowired
//    lateinit var kafkaConnectApi: KafkaConnectApi
//
//    @Autowired
//    lateinit var searchApi: SearchApi
//
//    @Autowired
//    lateinit var serviceMapper: ServiceMapper
//
//    @Autowired
//    lateinit var systemConfig: SystemConfig

//    /**
//     * Re-index Elasticsearch and sync with Postgres
//     */
//    @BeforeClass
//    fun beforeClass() {
//        flywayMigration.clean()
//        searchApi.clean()
//        kafkaConnectApi.clean()
//        kafkaConnectApi.init()
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
//        kafkaConnectApi.clean()
//    }
//
//    @BeforeMethod
//    fun beforeMethod(method: Method) {
//        log.info("Testing ${method.name}")
//    }
//
//    @Test
//    fun findCourseByUuid() {
//        val page = courseService.findCourseByUuid("187ad36d-a064-4847-a446-f579da149c75")
//        assertNotNull(page)
//        assertEquals(1, page.totalRecords)
//        assertEquals(493, page.records[0].id)
//        assertEquals("Draft", page.records[0].status)
//        assertEquals("Hellenistic Period,Root.History.Ancient_History.Hellenistic_Period", page.records[0].subject)
//        assertEquals("Advanced", page.records[0].level)
//        assertEquals("Live", page.records[0].instructionMode)
//        assertEquals(3, page.records[0].numCredits)
//        assertEquals(599, page.records[0].price)
//        assertEquals(6, page.records[0].weeklyEffort)
//        assertEquals("Hintings cated sluggishly", page.records[0].skills[2])
//    }
//
//    @DataProvider(name = "findAllCoursesTestCases")
//    fun findAllCoursesTestCases(): Array<Array<Any>> {
//        val sortBy0: MutableList<String> = arrayListOf()
//        val sortDir0: MutableList<String> = arrayListOf()
//
//        val sortBy1: MutableList<String> = arrayListOf("id", "name")
//        val sortDir1: MutableList<String> = arrayListOf("desc")
//
//        val sortBy2: MutableList<String> = arrayListOf("subject", "status", "level", "instruction_mode", "price")
//        val sortDir2: MutableList<String> = arrayListOf("asc", "desc", "desc", "asc", "asc")
//
//        val searchQuery = ""
//
//        return arrayOf(
//                arrayOf(
//                        0, 10, sortBy0, sortDir0,
//                        "ba71464b-f21f-4de8-a2cd-1dc2c11f3799",
//                        "ac257276-2ac6-42ec-8679-eb66d97d929b",
//                        "game elements and digital game design",
//                        "Weekly design challenges",
//                        61L, 606L, searchQuery
//                ),
//                arrayOf(
//                        0, 10, sortBy1, sortDir1,
//                        "ba71464b-f21f-4de8-a2cd-1dc2c11f3799",
//                        "ac257276-2ac6-42ec-8679-eb66d97d929b",
//                        "game elements and digital game design",
//                        "Weekly design challenges",
//                        61L, 606L, searchQuery
//                ),
//                arrayOf(
//                        0, 10, sortBy2, sortDir2,
//                        "7851dbd5-d09f-4066-bc41-43a9c73bdd22",
//                        "986c8781-00d4-46ae-b14b-1b3cb32cf21d",
//                        "flux de données et effectuer des transformations",
//                        "admis en matière de rédaction de contrats commerciaux",
//                        61L, 606L, searchQuery
//                ),
//                arrayOf(
//                        400, 25, sortBy2, sortDir2,
//                        "7a302078-84c8-4dcb-b535-b017f3b64891",
//                        "81533929-1356-4702-bc76-e4aa8d64aafd",
//                        "you will develop and test hypotheses",
//                        "group communication are essential for your professional",
//                        25L, 606L, searchQuery
//                ),
//                arrayOf(
//                        0, 10, sortBy0, sortDir0,
//                        "0793b124-7e53-422e-9933-5694bf4d70eb",
//                        "8ac3700e-fd67-42ce-973f-234a9048747d",
//                        "different statistical software packages",
//                        "fundamental mathematical modeling techniques",
//                        28L, 273L, "games are fun"
//                )
//        )
//    }
//
//    @Test(dataProvider = "findAllCoursesTestCases")
//    fun findAllCourses(testCase: Array<Any>) {
//        val offset: Int = testCase[0] as Int
//        val limit: Int = testCase[1] as Int
//        val sortBy: MutableList<String> = testCase[2] as MutableList<String>
//        val sortDir: MutableList<String> = testCase[3] as MutableList<String>
//        val uuidFirst: String = testCase[4] as String
//        val uuidLast: String = testCase[5] as String
//        val descriptionFirst: String = testCase[6] as String
//        val descriptionLast: String = testCase[7] as String
//        val numPages: Long = testCase[8] as Long
//        val totalRecords: Long = testCase[9] as Long
//        val searchQuery: String = testCase[10] as String
//
//        val pageableRequest = PageableRequest()
//        pageableRequest.offset = offset
//        pageableRequest.limit = limit
//        pageableRequest.sortBy = sortBy
//        pageableRequest.sortDir = sortDir
//        pageableRequest.query = searchQuery
//
//        val page = courseService.findAllCourses(pageableRequest)
//        assertNotNull(page)
//        assertEquals(totalRecords, page.totalRecords)
//        assertEquals(numPages, page.numPages)
//        assertEquals(uuidFirst, page.records[0].uuid.toString())
//        assertEquals(uuidLast, page.records[limit-1].uuid.toString())
//        assertEquals( true, page.records[0].description.contains(descriptionFirst))
//        assertEquals( true, page.records[limit-1].description.contains(descriptionLast))
//    }
//
//    @Test(dependsOnMethods = ["deleteCourse"])
//    fun createCourse() {
//        val courseDto = getCourseDto()
//        val response = courseService.createCourse(courseDto)
//        assertNotNull(response)
//        Thread.sleep(systemConfig.indexSyncDelay)
//        val page = courseService.findCourseByUuid(response.uuid.toString())
//        assertNotNull(page)
//        assertEquals(1, page.totalRecords)
//        assertEquals(1, page.records.size)
//        assertEquals(response.id, page.records[0].id)
//        assertEquals(response.name, page.records[0].name)
//        assertEquals(response.description, page.records[0].description)
//        assertEquals(response.location, page.records[0].location)
//        assertEquals(response.instructionMode, page.records[0].instructionMode)
//        assertEquals(response.status, page.records[0].status)
//        assertEquals(response.startTimeDt, utcToLocalTime(page.records[0].startTimeDt))
//        assertEquals(response.endTimeDt, utcToLocalTime(page.records[0].endTimeDt))
//    }
//
//    @Test
//    fun updateCourse() {
//        val uuid = "187ad36d-a064-4847-a446-f579da149c75"
//        val page = courseService.findCourseByUuid(uuid)
//        assertNotNull(page)
//        assertEquals(1, page.totalRecords)
//        val toUpdate = serviceMapper.copyDto(page.records[0])
//        toUpdate.status = "In Progress"
//        toUpdate.location = "Los Dos Hermanos"
//
//        // Update the course
//        courseService.updateCourse(toUpdate)
//
//        // Wait for index synchronization
//        Thread.sleep(systemConfig.indexSyncDelay)
//
//        val updated = courseService.findCourseByUuid(uuid)
//        assertNotNull(updated)
//        assertEquals(1, updated.totalRecords)
//        assertEquals(toUpdate.id, updated.records[0].id)
//        assertEquals(toUpdate.name, updated.records[0].name)
//        assertEquals(toUpdate.description, updated.records[0].description)
//        assertEquals(toUpdate.instructionMode, updated.records[0].instructionMode)
//        assertEquals("Los Dos Hermanos", updated.records[0].location)
//        assertEquals("In Progress", updated.records[0].status)
//        assertEquals(true, updated.records[0].updatedDt > toUpdate.updatedDt)
//    }
//
//    @Test(dependsOnMethods = ["findAllCourses"])
//    fun deleteCourse() {
//        val uuid =  "d2b500cf-cc62-4186-8e88-a6dbc1b13d74"
//        courseService.deleteCourse(uuid)
//
//        // Wait for index synchronization
//        Thread.sleep(systemConfig.indexSyncDelay)
//
//        val page = courseService.findCourseByUuid(uuid)
//        assertNotNull(page)
//        assertEquals(0, page.totalRecords)
//        assertEquals(0, page.records.size)
//    }
//
//    private fun getCourseDto(): CourseDto {
//        val requirements = arrayOf("Delta req", "Epsilon req", "Zeta req")
//        val learningObjectives = arrayOf("Delta objective", "Epsilon objective", "Zeta objective")
//        val skills = arrayOf("Delta skill", "Epsilon skill", "Zeta skill")
//        val dto = CourseDto()
//        val now = OffsetDateTime.now()
//        dto.id = null
//        dto.status = "Draft"
//        dto.level = "Introductory"
//        dto.subject = "Computer Science,Root.Engineering.Computer_Science"
//        dto.instructionMode = "Offline"
//        dto.name = "Near-Eastern Philosophy"
//        dto.description = "A broad overview of near-eastern philosophy"
//        dto.requirements = requirements.toMutableList()
//        dto.location = "University of Chicago"
//        dto.numCredits = 4
//        dto.price = 99
//        dto.weeklyEffort = 8
//        dto.avatar = "avatar not available"
//        dto.learningObjectives = learningObjectives.toMutableList()
//        dto.shortName = "SBTC"
//        dto.skills = skills.toMutableList()
//        dto.createdDt = now
//        dto.updatedDt = now
//        dto.startTimeDt = "09:30:00"
//        dto.endTimeDt = "11:45:59"
//        return dto
//    }
}