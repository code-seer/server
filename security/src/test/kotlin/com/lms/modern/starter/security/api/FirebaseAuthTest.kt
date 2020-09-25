package com.lms.modern.starter.security.api

import com.lms.modern.starter.security.SecurityTestConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.annotations.Test
import kotlin.test.assertNotNull

@SpringBootTest(classes = [SecurityTestConfiguration::class])
class FirebaseAuthTest: AbstractTestNGSpringContextTests() {

    @Autowired
    private lateinit var firebaseConfig: FirebaseConfig

    @Test
    fun credentialParserTest() {
        val googleCredentials =  firebaseConfig.getFirebaseCredentials()
        assertNotNull(googleCredentials)
    }


}