package io.learnet.api.security

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.firebase.auth.UserRecord
import io.learnet.api.ApiTestConfiguration
import io.learnet.util.properties.DemoUserProps
import io.learnet.util.properties.FirebaseProps
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.*
import org.springframework.test.annotation.DirtiesContext
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

@DirtiesContext
@SpringBootTest(classes = [ApiTestConfiguration::class],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringSecurityTest: AbstractTestNGSpringContextTests() {

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

    private val contextPath = "/api"
    private val host = "localhost"
    private var idToken: String? = null
    private var userRecord: UserRecord? = null

    /**
     *
     * Create a test user and log them in.
    */
    @BeforeClass
    fun beforeClass() {
        deleteUser(demoUserProps.email)
        userRecord = createUser(demoUserProps)
    }

    /**
     * Delete user
     */
    @AfterClass
    fun afterClass() {
        deleteUser(demoUserProps.email)
    }

    @BeforeMethod
    fun nameBefore(method: Method) {
        log.info("  Testcase: " + method.name)
    }

    @Test
    fun httpStatus200Test() {
        idToken = login(firebaseProps, objectMapper, demoUserProps.email)
        request("/permissions", "POST")
        // Get updated idToken after new permissions have been assigned
        idToken = login(firebaseProps, objectMapper, demoUserProps.email)
        val response = request("/profile?email=${demoUserProps.email}", "GET")
        assertNotNull(response)
        assertEquals(200, response.statusCodeValue)
    }

    @Test
    fun httpStatus403Test() {
        createClaims(userRecord!!, true)
        idToken = login(firebaseProps, objectMapper, demoUserProps.email)
        val response = request("/profile?email=${demoUserProps.email}", "GET")
        assertNotNull(response)
        assertEquals(403, response.statusCodeValue)
    }

    @Test
    fun httpStatus401Test() {
        idToken = ""
        val response = request("/profile?email=${demoUserProps.email}", "GET")
        assertNotNull(response)
        assertEquals(401, response.statusCodeValue)
        val typeRef: TypeReference<HashMap<String, Any>> = object : TypeReference<HashMap<String, Any>>() {}
        val map = objectMapper.readValue(response.body, typeRef)
        assertEquals("UNAUTHORIZED", map["error"])
    }

//    private fun request(url: String): ResponseEntity<String>? {
//        val uri = "http://${host}:${port}${url}"
//        val headers = HttpHeaders()
//        headers.contentType = MediaType.APPLICATION_JSON
//        idToken?.let { headers.setBearerAuth(it) }
//        val entity = HttpEntity<Any>(null, headers)
//        return restTemplate?.exchange(uri, HttpMethod.GET, entity, String::class.java)
//    }

    private fun request(url: String, method: String): ResponseEntity<String>? {
        val uri = "http://${host}:${port}${contextPath}${url}"
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        idToken?.let { headers.setBearerAuth(it) }
        val entity = HttpEntity<Any>(null, headers)
        if (method == "POST") {
            return restTemplate?.postForEntity(uri, entity, String::class.java)
        }
        return restTemplate?.exchange(uri, HttpMethod.GET, entity, String::class.java)
    }
}
