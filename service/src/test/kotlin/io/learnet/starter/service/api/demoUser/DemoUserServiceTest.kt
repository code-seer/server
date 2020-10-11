package io.learnet.starter.service.api.demoUser


import io.learnet.starter.data.migration.FlywayMigration
import io.learnet.starter.model.PageableRequest
import io.learnet.starter.service.ServiceTestConfiguration
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.annotations.*
import java.lang.reflect.Method
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest(classes = [ServiceTestConfiguration::class])
class DemoUserServiceTest: AbstractTestNGSpringContextTests() {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    lateinit var flywayMigration: FlywayMigration

    @Autowired
    lateinit var demoUserService: DemoUserService

    @BeforeClass
    fun beforeClass() {
        flywayMigration.init()
    }

    @BeforeMethod
    fun beforeMethod(method: Method) {
        log.info("Testing ${method.name}")
    }

    @Test
    fun findAllUsers() {
        val pageableRequest = PageableRequest()
        pageableRequest.offset = 0
        pageableRequest.limit = 25
        val page = demoUserService.findAllUsers(pageableRequest)
        assertNotNull(page)
        assertEquals(100, page.totalRecords)
        assertEquals(4, page.numPages)
    }
}
