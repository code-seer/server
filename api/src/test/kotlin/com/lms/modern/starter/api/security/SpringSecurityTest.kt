package com.lms.modern.starter.api.security

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.UserRecord
import com.lms.modern.starter.api.ApiTestConfiguration
import com.lms.modern.starter.model.DemoUserResponse
import com.lms.modern.starter.model.PageableRequest
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.configurationprocessor.json.JSONObject
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
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

    private var idToken: String? = null

    /**
     *
     * Create a test user and log them in.
    */
    @BeforeClass
    fun beforeClass() {
        deleteUser()
        createClaims(createUser())
        login()
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

    private fun deleteUser() {
        try {
            FirebaseAuth.getInstance().deleteUser(FirebaseAuth.getInstance().getUserByEmail(email).uid)
        } catch (e: FirebaseAuthException) {
            log.info(e.message)
        }
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
     * We weill generate a custom token here and set the header with it. We will then send the request object
     * with the Authorization header when making our http request. At this point we claim that the user
     * is logged in. The setting of the header with the custom token completes the login process. Typically
     * this would involve getting login credentials from the UI and sending those along to Firebase in the
     * client code. But since there is no Firebase Client SDK for Java (only Android), we cannot perform
     * the routine login steps here. Instead, we bypass the UI part of the process.
     *
     * Once Spring Boot receives the request, its security filter should call the Firebase
     * Admin SDK and verify the token and decode the claims.
     */
    private fun login() {
        val userRecord = FirebaseAuth.getInstance().getUserByEmail(email)
        val customToken = FirebaseAuth.getInstance().createCustomToken(userRecord.uid)
        val url = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithCustomToken"
        val apiKey = "AIzaSyCnkdkr0Q2Ljby4qjcIECSAcGfvphpkC8k"
        val httpClient = HttpClients.createDefault()
        val json = JSONObject()
        json.put("token", customToken)
        json.put("returnSecureToken", true)
        val params = StringEntity(json.toString())
        val request = HttpPost("${url}?key=${apiKey}")
        request.addHeader("content-type", "application/json")
        request.entity = params;
        val typeRef: TypeReference<HashMap<String, Any>> = object : TypeReference<HashMap<String, Any>>() {}
        val map = objectMapper.readValue(httpClient.execute(request).entity.content, typeRef)
        httpClient.close()
        assertNotNull(map)
        idToken = map["idToken"] as String?
        assertNotNull(idToken)
    }

    @Test
    fun demo_user_endpoint_test() {
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
        val response = restTemplate?.postForEntity(uri, entity, String::class.java)
        assertNotNull(response)
        assertEquals(200, response.statusCodeValue)
        val demoUserResponse = objectMapper.readValue(response.body, DemoUserResponse::class.java)
        assertNotNull(demoUserResponse)
        assertEquals(4, demoUserResponse.numPages)
        assertEquals(100, demoUserResponse.totalRecords)
        assertEquals("Angelo", demoUserResponse.content[7].firstName)
        assertEquals("Carter", demoUserResponse.content[7].lastName)
    }
}