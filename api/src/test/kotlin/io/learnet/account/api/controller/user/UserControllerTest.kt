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

    private val contextPath = "/account"
    private val host = "localhost"
    private var idToken: String? = null
    private var userRecord: UserRecord? = null
    private var userProfileDto: UserProfileDto? = null
    private var userSocialDto: UserSocialDto? = null

    @BeforeClass
    fun beforeClass() {
        deleteUser(demoUserProps.email)
        userRecord = createUser(demoUserProps)
        idToken = login(firebaseProps, objectMapper, demoUserProps.email)
        createUserProfileTestClaims()
        userProfileDto = createTestUserProfile()
        userSocialDto = createTestUserSocial()
    }

    @AfterClass
    fun afterClass() {
        deleteUser(demoUserProps.email)
    }

    @BeforeMethod
    fun nameBefore(method: Method) {
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

    private fun createUserProfileTestClaims() {
        val request = UserPermissionsRequest()
        request.email = demoUserProps.email
        request.displayName = "UserController Test"
        val response = request("permissions", "POST", request)
        assertEquals(200, response?.statusCodeValue)
    }

    private fun createTestUserProfile(): UserProfileDto? {
        val faker = Faker()
        val userProfileDto = UserProfileDto()
        userProfileDto.firstName = faker.name().firstName()
        userProfileDto.lastName = faker.name().lastName()
        userProfileDto.title = faker.name().title()
        userProfileDto.email = demoUserProps.email
        userProfileDto.isNewUser = true
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
    fun save_user_profile_test() {
        val response = request("profile", "POST", userProfileDto)
        assertNotNull(response)
        assertEquals(200, response.statusCodeValue)
        val responseDto = objectMapper.readValue(response.body, UserProfileDto::class.java)
       assertUserProfileResponse(responseDto)
    }

    @Test
    fun save_user_social_test() {

    }

    @Test(dependsOnMethods = ["save_user_profile_test"])
    fun get_user_profile_test() {
        val response = request("profile?email=${demoUserProps.email}", "GET", null)
        assertNotNull(response)
        assertEquals(200, response.statusCodeValue)
        val responseDto = objectMapper.readValue(response.body, UserProfileDto::class.java)
        assertUserProfileResponse(responseDto)
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




//    private fun getSocial(): SocialEntity? {
//        val faker = Faker()
//        val entity = SocialEntity()
//        val now = OffsetDateTime.now()
//        entity.facebook = faker.internet().url()
//        entity.twitter = faker.name().username()
//        entity.linkedin = faker.internet().url()
//        entity.github = faker.internet().url()
//        entity.youtube = faker.internet().url()
//        entity.instagram = faker.name().username()
//        entity.snapchat = faker.name().username()
//        entity.whatsapp = faker.phoneNumber().phoneNumber()
//        entity.website = faker.internet().url()
//        entity.createdDt = now
//        entity.updatedDt = now
//        entity.uuid = UUID.randomUUID()
//        return socialRepo.save(entity)
//    }

//    private fun getAddress(): AddressEntity {
//        val faker = Faker()
//        val entity = AddressEntity()
//        val now = OffsetDateTime.now()
//        entity.country = faker.address().country()
//        entity.state = faker.address().stateAbbr()
//        entity.city = faker.address().city()
//        entity.postalCode = faker.address().zipCode()
//        entity.address1 = faker.address().fullAddress()
//        entity.createdDt = now
//        entity.updatedDt = now
//        entity.uuid = UUID.randomUUID()
//        return addressRepo.save(entity)
//    }


//    @Test
//    fun demo_user_endpoint_test() {
//        createClaims(userRecord!!, false)
//        idToken = login(firebaseProps, objectMapper, demoUserProps.email)
//        val response = request()
//        assertNotNull(response)
//        assertEquals(200, response.statusCodeValue)
//        val demoUserResponse = objectMapper.readValue(response.body, DemoUserResponse::class.java)
//        assertNotNull(demoUserResponse)
//        assertEquals(4, demoUserResponse.numPages)
//        assertEquals(100, demoUserResponse.totalRecords)
//        assertEquals("Angelo", demoUserResponse.content[7].firstName)
//        assertEquals("Carter", demoUserResponse.content[7].lastName)
//    }

    private fun request(url: String, method: String, dto: Any?): ResponseEntity<String>? {
        val uri = "http://${host}:${port}${contextPath}/${url}"
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        idToken?.let { headers.setBearerAuth(it) }
        val entity = HttpEntity<Any>(dto, headers)
        if (method == "POST") {
            return restTemplate?.postForEntity(uri, entity, String::class.java)
        }
        return restTemplate?.getForEntity(uri, String::class.java)
    }
}
