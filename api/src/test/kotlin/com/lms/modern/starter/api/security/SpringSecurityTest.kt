package com.lms.modern.starter.api.security

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.firebase.auth.UserRecord
import com.lms.modern.starter.api.*
import com.lms.modern.starter.model.DemoUserResponse
import com.lms.modern.starter.model.PageableRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import java.lang.reflect.Method
import java.util.HashMap
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


/**
 * Test Spring Security with Firebase using MockMvc.
 */
@AutoConfigureMockMvc
@SpringBootTest(classes = [ApiTestConfiguration::class],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringSecurityTest: AbstractTestNGSpringContextTests() {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    @LocalServerPort
    private val port = 0

    @Autowired
    private lateinit var firebaseConfig: FirebaseConfig

    @Autowired
    private val restTemplate: TestRestTemplate? = null

    @Autowired
    @Qualifier("jacksonObjectMapper")
    lateinit var objectMapper: ObjectMapper

    private val testUrl = "/api/starter/demoUser/findAllUsers"
    private val host = "localhost"
    private var idToken: String? = null
    private var userRecord: UserRecord? = null

    /**
     *
     * Create a test user and log them in.
    */
    @BeforeClass
    fun beforeClass() {
        deleteUser()
        userRecord = createUser()
    }

    /**
     * Delete user
     */
    @AfterClass
    fun afterClass() {
        deleteUser()
    }

    @BeforeMethod
    fun nameBefore(method: Method) {
        log.info("  Testcase: " + method.name)
    }

    @Test
    fun demo_user_endpoint_200_test() {
        createClaims(userRecord!!, false)
        idToken = login(firebaseConfig, objectMapper)
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

    @Test
    fun demo_user_endpoint_403_test() {
        createClaims(userRecord!!, true)
        idToken = login(firebaseConfig, objectMapper)
        val response = request()
        assertNotNull(response)
        assertEquals(403, response.statusCodeValue)
    }

    @Test
    fun demo_user_endpoint_401_test() {
        idToken = ""
        val response = request()
        assertNotNull(response)
        assertEquals(401, response.statusCodeValue)
        val typeRef: TypeReference<HashMap<String, Any>> = object : TypeReference<HashMap<String, Any>>() {}
        val map = objectMapper.readValue(response.body, typeRef)
        assertEquals("UNAUTHORIZED", map["error"])
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