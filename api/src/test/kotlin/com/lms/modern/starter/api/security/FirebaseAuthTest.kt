package com.lms.modern.starter.api.security

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.UserRecord
import com.lms.modern.starter.api.*
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

    @BeforeClass
    fun beforeClass() {
        try {
            FirebaseAuth.getInstance().deleteUser(FirebaseAuth.getInstance().getUserByEmail(email).uid)
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
    }

    @Test(priority = 2)
    fun create_custom_claims_test() {
        val userRecord = FirebaseAuth.getInstance().getUserByEmail(email)
        assertNotNull(userRecord)
        assertEquals(userRecord.displayName, displayName)
        val claims =  hashMapOf<String, Boolean>()
        claims["ROLE_ADMIN"] = true
        claims["ROLE_TEST"] = true
        claims["ROLE_PREMIUM_USER"] = true
        FirebaseAuth.getInstance().setCustomUserClaims(userRecord.uid, claims as Map<String, Any>?)
        val newUserRecord = FirebaseAuth.getInstance().getUserByEmail(email)
        assertNotNull(newUserRecord)
        assertEquals(newUserRecord.uid, userRecord.uid)
        assertEquals(newUserRecord.customClaims.size, 3)
    }

    @Test(priority = 3)
    fun update_custom_claims_test() {
        val userRecord = FirebaseAuth.getInstance().getUserByEmail(email)
        assertNotNull(userRecord)
        assertEquals(userRecord.displayName, displayName)
        val claims = userRecord.customClaims.toMutableMap()
        claims["ROLE_AFFLUENT_USER"] = true
        claims["ROLE_VIP_USER"] = true
        FirebaseAuth.getInstance().setCustomUserClaims(userRecord.uid, claims as Map<String, Any>?)
        val newUserRecord = FirebaseAuth.getInstance().getUserByEmail(email)
        assertNotNull(newUserRecord)
        assertEquals(newUserRecord.uid, userRecord.uid)
        assertEquals(newUserRecord.customClaims.size, 5)
    }

    @Test(priority = 4)
    fun delete_user_test() {
        val error = "No user record found for the provided email: $email"
        var asserted = false
        try {
            FirebaseAuth.getInstance().deleteUser(FirebaseAuth.getInstance().getUserByEmail(email).uid)
            FirebaseAuth.getInstance().getUserByEmail(email)
        } catch (e: FirebaseAuthException) {
            assertEquals(e.message, error)
            asserted = true
        }
        finally {
            assertEquals(true, asserted)
        }
    }

}