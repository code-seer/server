package io.learnet.service.api.user


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.UserRecord
import io.learnet.account.model.UserDto
import io.learnet.service.ServiceTestConfiguration
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
class UserManagementTest: AbstractTestNGSpringContextTests() {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    lateinit var userManagement: UserManagement
    
    @Autowired
    lateinit var userDto: UserDto

    @BeforeClass
    fun beforeClass() {
        log.info("Test class ${this.javaClass.simpleName} started")
        deleteUser()
        val request: UserRecord.CreateRequest = UserRecord.CreateRequest()
                .setEmail(userDto.email)
                .setEmailVerified(false)
                .setDisabled(false)
        FirebaseAuth.getInstance().createUser(request)
        log.info("Test user with email address $userDto.email has been created")
    }

    @AfterClass
    fun afterClass() {
        deleteUser()
        log.info("Test class ${this.javaClass.simpleName} ended")
    }

    @BeforeMethod
    fun beforeMethod(method: Method) {
        log.info("Test method ${method.name} started")
    }

    @AfterMethod
    fun afterMethod(method: Method) {
        log.info("Test method ${method.name} ended")
    }

    private fun deleteUser() {
        try {
            FirebaseAuth.getInstance().deleteUser(FirebaseAuth.getInstance().getUserByEmail(userDto.email).uid)
            log.info("Test user with email address $userDto.email has been deleted")
        } catch (e: FirebaseAuthException) {
            log.info("Test user with email address $userDto.email does not exist")
        }
    }

    @Test(priority = 1)
    fun createPermissions() {
        val response = userManagement.createPermissions()
        assertNotNull(response)
        assertEquals("OK", response)

        val expected = listOf(
            "ROLE_DEMO_USER_READ",
            "ROLE_VIEW_DASHBOARD",
            "ROLE_CREATE_CONFERENCE",
            "ROLE_WRITE_USER_PROFILE",
            "ROLE_READ_USER_PROFILE").toSet()
        val userRecord = FirebaseAuth.getInstance().getUserByEmail(userDto.email)
        assertNotNull(userRecord)
        assertEquals(expected.size, userRecord.customClaims.size)
        val actual = userRecord.customClaims.toList()
        for (role in actual) {
            assertEquals(true, expected.contains(role.first))
        }
    }

    @Test(priority = 2)
    fun createPermissionsWithNoChange() {
        val response = userManagement.createPermissions()
        assertNotNull(response)
        assertEquals("No Change", response)
    }

}
