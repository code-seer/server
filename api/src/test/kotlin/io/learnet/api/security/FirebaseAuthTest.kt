package io.learnet.api.security

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.UserRecord
import io.learnet.api.ApiTestConfiguration
import io.learnet.account.util.FirebaseConfig
import io.learnet.account.util.properties.DemoUserProps
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


@SpringBootTest(classes = [ApiTestConfiguration::class])
class FirebaseAuthTest: AbstractTestNGSpringContextTests() {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    private lateinit var firebaseConfig: FirebaseConfig

    @Autowired
    private lateinit var demoUserProps: DemoUserProps

    @BeforeClass
    fun beforeClass() {
        try {
            FirebaseAuth.getInstance().deleteUser(FirebaseAuth.getInstance().getUserByEmail(demoUserProps.email).uid)
        } catch (e: FirebaseAuthException) {
            log.info(e.message)
        }
    }

    @Test(priority = 0)
    fun credentials_parser_test() {
        val googleCredentials =  firebaseConfig.getFirebaseCredentials()
        assertNotNull(googleCredentials)
    }

    @Test(priority = 1)
    fun create_user_test() {
        val request: UserRecord.CreateRequest = UserRecord.CreateRequest()
                .setEmail(demoUserProps.email)
                .setEmailVerified(false)
                .setPassword(demoUserProps.password)
                .setPhoneNumber(demoUserProps.phoneNumber)
                .setDisplayName(demoUserProps.displayName)
                .setPhotoUrl(demoUserProps.photoUrl)
                .setDisabled(false)
        val userRecord = FirebaseAuth.getInstance().createUser(request)
        assertEquals(userRecord.displayName, demoUserProps.displayName)
        assertEquals(userRecord.email, demoUserProps.email)
        assertEquals(userRecord.phoneNumber, demoUserProps.phoneNumber)
        assertEquals(userRecord.photoUrl, demoUserProps.photoUrl)
    }

    @Test(priority = 2)
    fun create_custom_claims_test() {
        val userRecord = FirebaseAuth.getInstance().getUserByEmail(demoUserProps.email)
        assertNotNull(userRecord)
        assertEquals(userRecord.displayName, demoUserProps.displayName)
        val claims =  hashMapOf<String, Boolean>()
        claims["ROLE_ADMIN"] = true
        claims["ROLE_TEST"] = true
        claims["ROLE_PREMIUM_USER"] = true
        FirebaseAuth.getInstance().setCustomUserClaims(userRecord.uid, claims as Map<String, Any>?)
        val newUserRecord = FirebaseAuth.getInstance().getUserByEmail(demoUserProps.email)
        assertNotNull(newUserRecord)
        assertEquals(newUserRecord.uid, userRecord.uid)
        assertEquals(newUserRecord.customClaims.size, 3)
    }

    @Test(priority = 3)
    fun update_custom_claims_test() {
        val userRecord = FirebaseAuth.getInstance().getUserByEmail(demoUserProps.email)
        assertNotNull(userRecord)
        assertEquals(userRecord.displayName, demoUserProps.displayName)
        val claims = userRecord.customClaims.toMutableMap()
        claims["ROLE_AFFLUENT_USER"] = true
        claims["ROLE_VIP_USER"] = true
        FirebaseAuth.getInstance().setCustomUserClaims(userRecord.uid, claims as Map<String, Any>?)
        val newUserRecord = FirebaseAuth.getInstance().getUserByEmail(demoUserProps.email)
        assertNotNull(newUserRecord)
        assertEquals(newUserRecord.uid, userRecord.uid)
        assertEquals(newUserRecord.customClaims.size, 5)
    }

    @Test(priority = 4)
    fun delete_user_test() {
        val error = "No user record found for the provided email: ${demoUserProps.email}"
        var asserted = false
        try {
            FirebaseAuth.getInstance().deleteUser(FirebaseAuth.getInstance().getUserByEmail(demoUserProps.email).uid)
            FirebaseAuth.getInstance().getUserByEmail(demoUserProps.email)
        } catch (e: FirebaseAuthException) {
            assertEquals(e.message, error)
            asserted = true
        }
        finally {
            assertEquals(true, asserted)
        }
    }

}
