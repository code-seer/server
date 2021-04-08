package io.learnet.account.api.controller.user

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.javafaker.Faker
import com.google.firebase.auth.UserRecord
import io.learnet.account.api.ApiTestConfiguration
import io.learnet.account.api.security.createUser
import io.learnet.account.api.security.deleteUser
import io.learnet.account.api.security.login
import io.learnet.account.model.*
import io.learnet.account.util.properties.DemoUserProps
import io.learnet.account.util.properties.FirebaseProps
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
import java.io.File
import java.lang.reflect.Method
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import org.springframework.util.LinkedMultiValueMap


@DirtiesContext
@SpringBootTest(classes = [ApiTestConfiguration::class],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest: AbstractTestNGSpringContextTests() {

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
    private var userProfileDto: UserProfileDto? = null
    private var userSocialDto: UserSocialDto? = null

    @BeforeClass
    fun beforeClass() {
        deleteUser(demoUserProps.email)
        userRecord = createUser(demoUserProps)
        userProfileDto = createTestUserProfile()
        userSocialDto = createTestUserSocial()
    }

    @AfterClass
    fun afterClass() {
        deleteUser(demoUserProps.email)
    }

    @BeforeMethod
    fun nameBefore(method: Method) {
        idToken = login(firebaseProps, objectMapper, demoUserProps.email)
        log.info("  Testcase: " + method.name)
    }

    private fun createTestUserSocial(): UserSocialDto? {
        val faker = Faker()
        val socialDto = UserSocialDto()
        socialDto.facebook = faker.internet().url()
        socialDto.twitter = faker.name().username()
        socialDto.linkedin = faker.internet().url()
        socialDto.github = faker.internet().url()
        socialDto.instagram = faker.name().username()
        socialDto.whatsapp = faker.phoneNumber().phoneNumber()
        socialDto.website = faker.internet().url()
        return socialDto
    }


    private fun createTestUserProfile(): UserProfileDto? {
        val faker = Faker()
        val userProfileDto = UserProfileDto()
        userProfileDto.firstName = faker.name().firstName()
        userProfileDto.lastName = faker.name().lastName()
        userProfileDto.title = faker.name().title()
        userProfileDto.email = demoUserProps.email
        userProfileDto.homePhone = faker.phoneNumber().phoneNumber()
        userProfileDto.mobilePhone = faker.phoneNumber().cellPhone()
        userProfileDto.country = faker.address().country()
        userProfileDto.state = faker.address().stateAbbr()
        userProfileDto.city = faker.address().city()
        userProfileDto.postalCode = faker.address().zipCode()
        userProfileDto.address = faker.address().fullAddress()
        return userProfileDto
    }

    @Test
    fun create_user_permissions() {
        val response = request("permissions", "POST", false, null)
        assertNotNull(response)
        assertEquals(200, response.statusCodeValue)
    }


    @Test(dependsOnMethods = ["create_user_permissions"])
    fun save_user_profile() {
        val response = request("profile", "POST", false, userProfileDto)
        assertNotNull(response)
        assertEquals(200, response.statusCodeValue)
        val responseDto = objectMapper.readValue(response.body, UserProfileDto::class.java)
       assertUserProfileResponse(responseDto)
    }

    @Test(dependsOnMethods = ["create_user_permissions"])
    fun save_user_social() {
        val response = request("social", "POST", false, userSocialDto)
        assertNotNull(response)
        assertEquals(200, response.statusCodeValue)
        val responseDto = objectMapper.readValue(response.body, UserSocialDto::class.java)
        assertNotNull(responseDto)
        assertEquals(userSocialDto?.facebook, responseDto.facebook)
        assertEquals(userSocialDto?.instagram, responseDto.instagram)
    }

    @Test(dependsOnMethods = ["save_user_profile"])
    fun get_user_profile() {
        val response = request("profile?email=${demoUserProps.email}", "GET", false, null)
        assertNotNull(response)
        assertEquals(200, response.statusCodeValue)
        val responseDto = objectMapper.readValue(response.body, UserProfileDto::class.java)
        assertUserProfileResponse(responseDto)
    }

    // TODO: unable to load a multipart file using the dispatch servlet
    @Test(dependsOnMethods = ["create_user_permissions"], enabled = false)
    fun upload_avatar() {
        val requestBody: LinkedMultiValueMap<Any, Any> = LinkedMultiValueMap<Any, Any>()
        requestBody.add("avatar", loadAvatar())
        requestBody.add("email", demoUserProps.email)
        val response = request("avatar", "POST", true, requestBody)
        assertNotNull(response)
        assertEquals(200, response.statusCodeValue)
        val responseDto = objectMapper.readValue(response.body, UserAvatarResponse::class.java)
        assertNotNull(responseDto)
        assertEquals(true, responseDto.url.contains("amazon"))
    }

    @Test(dependsOnMethods = ["upload_avatar"], enabled = false)
    fun get_avatar() {
        val response = request("avatar?email=${demoUserProps.email}", "GET", false,null)
        assertNotNull(response)
        assertEquals(200, response.statusCodeValue)
        val responseDto = objectMapper.readValue(response.body, UserAvatarResponse::class.java)
        assertNotNull(responseDto)
        assertEquals(true, responseDto.url.contains("amazon"))
    }

    private fun assertUserProfileResponse(responseDto: UserProfileDto?) {
        assertNotNull(responseDto)
        assertEquals(userProfileDto?.firstName, responseDto.firstName)
        assertEquals(userProfileDto?.lastName, responseDto.lastName)
        assertEquals(userProfileDto?.title, responseDto.title)
        assertEquals(userProfileDto?.email, responseDto.email)
        assertEquals(userProfileDto?.homePhone, responseDto.homePhone)
        assertEquals(userProfileDto?.mobilePhone, responseDto.mobilePhone)
        assertEquals(userProfileDto?.country, responseDto.country)
        assertEquals(userProfileDto?.state, responseDto.state)
        assertEquals(userProfileDto?.city, responseDto.city)
        assertEquals(userProfileDto?.postalCode, responseDto.postalCode)
        assertEquals(userProfileDto?.address, responseDto.address)
    }

    private fun request(url: String, method: String, multiPart: Boolean, dto: Any?): ResponseEntity<String>? {
        val uri = "http://${host}:${port}${contextPath}/${url}"
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        if (multiPart) {
            headers.contentType = MediaType.MULTIPART_FORM_DATA
        }
        idToken?.let { headers.setBearerAuth(it) }
        val entity = HttpEntity<Any>(dto, headers)
        if (method == "POST") {
            return restTemplate?.postForEntity(uri, entity, String::class.java)
        }
        return restTemplate?.exchange(uri, HttpMethod.GET, entity, String::class.java)
    }

    private fun loadAvatar(): File {
        return File(this::class.java.getResource("/avatar/dugg.jpg").file)
    }
}
