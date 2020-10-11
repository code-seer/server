package io.learnet.starter.api.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.firebase.auth.UserRecord
import io.learnet.starter.api.*
import io.learnet.starter.util.properties.DemoUserProps
import io.learnet.starter.util.properties.FirebaseProps
import io.learnet.starter.api.security.*
import io.learnet.starter.model.DemoUserResponse
import io.learnet.starter.model.PageableRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import java.lang.reflect.Method
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


@DirtiesContext
@SpringBootTest(classes = [ApiTestConfiguration::class],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiTest: AbstractTestNGSpringContextTests() {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    @LocalServerPort
    private val port = 0

    @Autowired
    private lateinit var firebaseProps: FirebaseProps

    @Autowired
    private lateinit var demoUserProps: DemoUserProps

    @Autowired
    private val restTemplate: TestRestTemplate? = null

    @Autowired
    @Qualifier("jacksonObjectMapper")
    lateinit var objectMapper: ObjectMapper

    private val testUrl = "/api/starter/demoUser/findAllUsers"
    private val host = "localhost"
    private var idToken: String? = null
    private var userRecord: UserRecord? = null

    @BeforeClass
    fun beforeClass() {
        deleteUser(demoUserProps.email)
        userRecord = createUser(demoUserProps)
    }

    @AfterClass
    fun afterClass() {
        deleteUser(demoUserProps.email)
    }

    @BeforeMethod
    fun nameBefore(method: Method) {
        log.info("  Testcase: " + method.name)
    }


    @Test
    fun demo_user_endpoint_test() {
        createClaims(userRecord!!, false)
        idToken = login(firebaseProps, objectMapper, demoUserProps.email)
        val response = request()
        assertNotNull(response)
        assertEquals(200, response.statusCodeValue)
        val demoUserResponse = objectMapper.readValue(response.body, DemoUserResponse::class.java)
        assertNotNull(demoUserResponse)
        assertEquals(4, demoUserResponse.numPages)
        assertEquals(100, demoUserResponse.totalRecords)
        assertEquals("Angelo", demoUserResponse.content[7].firstName)
        assertEquals("Carter", demoUserResponse.content[7].lastName)
    }

    private fun request(): ResponseEntity<String>? {
        val uri = "http://${host}:${port}${testUrl}"
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        idToken?.let { headers.setBearerAuth(it) }
        val pageableRequest = PageableRequest()
        pageableRequest.limit = 25
        pageableRequest.offset = 0
        pageableRequest.sortBy = arrayOf("lastName").toMutableList()
        pageableRequest.sortDir = arrayOf("ASC").toMutableList()
        val entity = HttpEntity<PageableRequest>(pageableRequest, headers)
        return restTemplate?.postForEntity(uri, entity, String::class.java)
    }
}
