package com.lms.modern.starter.api.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.UserRecord
import com.lms.modern.starter.api.ApiTestConfiguration
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
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import java.lang.reflect.Method
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
    private val restTemplate: TestRestTemplate? = null

    @Autowired
    @Qualifier("jacksonObjectMapper")
    lateinit var objectMapper: ObjectMapper

    private val testUrl = "/api/starter/demoUser/findAllUsers"
    private val host = "localhost"
    private val displayName = "LMS TestNG User"
    private val email = "lms.testng@modern-lms.com"
    private val password = "secretPassword"
    private val phoneNumber = "+11234567890"
    private val photoUrl = "http://www.example.com/12345678/photo.png"

    private var customToken: String? = null

    /**
     *
     * Create a test user and log them in.
    */
    @BeforeClass
    fun beforeClass() {
        createClaims(createUser())
        login()

    }

    @BeforeMethod
    fun nameBefore(method: Method) {
        log.info("  Testcase: " + method.name)
    }

    private fun createUser(): UserRecord {
        val request: UserRecord.CreateRequest = UserRecord.CreateRequest()
                .setEmail(email)
                .setEmailVerified(false)
                .setPassword(password)
                .setPhoneNumber(phoneNumber)
                .setDisplayName(displayName)
                .setPhotoUrl(photoUrl)
                .setDisabled(false)
        val userRecord = FirebaseAuth.getInstance().createUser(request)
        assertEquals(userRecord.displayName, displayName)
        assertEquals(userRecord.email, email)
        assertEquals(userRecord.phoneNumber, phoneNumber)
        assertEquals(userRecord.photoUrl, photoUrl)
        return userRecord
    }

    private fun createClaims(userRecord: UserRecord) {
        val claims =  hashMapOf<String, Boolean>()
        claims["ROLE_ADMIN"] = true
        claims["ROLE_TEST"] = true
        claims["ROLE_DEMO_USER_READ"] = true
        FirebaseAuth.getInstance().setCustomUserClaims(userRecord.uid, claims as Map<String, Any>?)
        val newUserRecord = FirebaseAuth.getInstance().getUserByEmail(email)
        assertNotNull(newUserRecord)
        assertEquals(newUserRecord.uid, userRecord.uid)
        assertEquals(newUserRecord.customClaims.size, 3)
    }

    /**
     * Login the newly created user. Firebase gives us the option of handling login on the client side
     * by calling the Firebase Client SDK directly or by generating a custom token on the server side.
     * We weill generate a custom token here set the header with it. We will then send the request object
     * with the Authorization header when making our http request. At this point we claim that the user
     * is logged in. The setting of the header with the custom token completes the login process. Typically
     * this would involve getting login credentials from the UI and sending those along to firebase in the
     * client code. But since there is no Firebase Client SDK for Java (only Android), we cannot perform
     * the routine login steps here. Instead, we bypass the UI part of the process.
     *
     * Once Spring Boot receives the request, its security filter should call the Firebase
     * Admin SDK and verify the token and decode the claims.
     */
    private fun login() {
        val userRecord = FirebaseAuth.getInstance().getUserByEmail(email)
        customToken = FirebaseAuth.getInstance().createCustomToken(userRecord.uid)
        assertNotNull(customToken)
    }

    /**
     * Delete user
     */
    @AfterClass
    fun afterClass() {
        try {
            FirebaseAuth.getInstance().deleteUser(FirebaseAuth.getInstance().getUserByEmail(email).uid)
        } catch (e: FirebaseAuthException) {
            log.info(e.message)
        }
    }

    @Test
    fun demo_user_endpoint_test() {
        val uri = "http://${host}:${port}${testUrl}"
        val headers = HttpHeaders()
        val request = PageableRequest()
        request.limit = 25
        request.offset = 0
        request.sortBy = arrayOf("lastName").toMutableList()
        request.sortDir = arrayOf("ASC").toMutableList()
        headers.contentType = MediaType.APPLICATION_JSON
        val response = restTemplate?.postForEntity(uri, request, String::class.java)
        assertNotNull(response)
        assertEquals(200, response.statusCodeValue)
        val demoUserResponse = objectMapper.readValue(response.body, DemoUserResponse::class.java)
        assertNotNull(demoUserResponse)
        assertEquals(4, demoUserResponse.numPages)
        assertEquals(100, demoUserResponse.totalRecords)
        assertEquals("Milissa", demoUserResponse.content[6].firstName)
        assertEquals("Brown", demoUserResponse.content[6].lastName)
    }
}