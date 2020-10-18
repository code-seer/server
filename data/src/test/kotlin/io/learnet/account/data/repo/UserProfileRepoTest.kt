package io.learnet.account.data.repo

import com.github.javafaker.Faker
import io.learnet.account.data.DataTestConfiguration
import io.learnet.account.data.migration.FlywayMigration
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.springframework.transaction.annotation.Transactional
import org.testng.annotations.BeforeClass
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import java.lang.reflect.Method
import java.time.OffsetDateTime
import java.util.*
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
    lateinit var flywayMigration: FlywayMigration

    @BeforeClass
    fun beforeClass() {
//        flywayMigration.init()
    }

    @BeforeMethod
    open fun beforeMethod(method: Method) {
        log.info("Testing ${method.name}")
    }

    @Test(priority = 0)
    open fun find_all_user_profiles_test() {
        var response = userProfileRepo.findAll()
        assertNotNull(response)
        assertEquals(false, response.isEmpty())
        assertEquals(100, response.size)
    }

    @Test(priority = 0)
    open fun find_all_addresses_test() {
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
}
