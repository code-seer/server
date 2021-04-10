package io.learnet.data.repo

import io.learnet.data.DataTestConfiguration
import io.learnet.data.migration.FlywayMigration
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.springframework.transaction.annotation.Transactional
import org.testng.annotations.BeforeClass
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import java.lang.reflect.Method
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@Transactional
@SpringBootTest(classes = [DataTestConfiguration::class])
open class UserProfileRepoTest: AbstractTestNGSpringContextTests() {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    lateinit var userProfileRepo: UserProfileRepo

    @Autowired
    lateinit var addressRepo: AddressRepo

    @Autowired
    lateinit var socialRepo: SocialRepo

    @Autowired
    lateinit var securityRepo: SecurityRepo

    @Autowired
    lateinit var userSettingsRepo: UserSettingsRepo

    @Autowired
    lateinit var flywayMigration: FlywayMigration

    @BeforeClass
    fun beforeClass() {
        flywayMigration.init()
    }

    @BeforeMethod
    open fun beforeMethod(method: Method) {
        log.info("Testing ${method.name}")
    }

    @Test
    open fun find_all_user_profile_entities_test() {
        var response = userProfileRepo.findAll()
        assertNotNull(response)
        assertEquals(false, response.isEmpty())
        assertEquals(100, response.size)
    }

    @Test
    open fun find_all_address_entities_test() {
        var response = addressRepo.findAll()
        assertNotNull(response)
        assertEquals(false, response.isEmpty())
        assertEquals(100, response.size)

        val profileResponse = userProfileRepo.findAll()
        assertNotNull(profileResponse)
        assertEquals(false, profileResponse.isEmpty())
        assertEquals(100, profileResponse.size)
        assertNotNull(profileResponse[0].address)
    }

    @Test
    open fun find_all_social_entities_test() {
        var response = socialRepo.findAll()
        assertNotNull(response)
        assertEquals(false, response.isEmpty())
        assertEquals(100, response.size)

        val profileResponse = userProfileRepo.findAll()
        assertNotNull(profileResponse)
        assertEquals(false, profileResponse.isEmpty())
        assertEquals(100, profileResponse.size)
        assertNotNull(profileResponse[0].social)
    }

    @Test
    open fun find_all_security_entities_test() {
        var response = securityRepo.findAll()
        assertNotNull(response)
        assertEquals(false, response.isEmpty())
        assertEquals(100, response.size)

        val profileResponse = userProfileRepo.findAll()
        assertNotNull(profileResponse)
        assertEquals(false, profileResponse.isEmpty())
        assertEquals(100, profileResponse.size)
        assertNotNull(profileResponse[0].security)
    }

    @Test
    open fun find_all_user_settings_entities_test() {
        var response = userSettingsRepo.findAll()
        assertNotNull(response)
        assertEquals(false, response.isEmpty())
        assertEquals(100, response.size)

        val profileResponse = userProfileRepo.findAll()
        assertNotNull(profileResponse)
        assertEquals(false, profileResponse.isEmpty())
        assertEquals(100, profileResponse.size)
        assertNotNull(profileResponse[0].userSettings)
    }
}
