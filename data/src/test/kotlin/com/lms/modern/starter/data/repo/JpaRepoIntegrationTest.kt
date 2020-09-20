package com.lms.modern.starter.data.repo

import com.github.javafaker.Faker
import com.lms.modern.starter.config.SystemConfigConfiguration
import com.lms.modern.starter.data.DataTestConfiguration
import com.lms.modern.starter.data.entity.DemoUserEntity
import com.lms.modern.starter.data.migration.FlywayMigration
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
@SpringBootTest(classes = [DataTestConfiguration::class, SystemConfigConfiguration::class])
open class JpaRepoIntegrationTest: AbstractTestNGSpringContextTests() {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    lateinit var demoUserRepo: DemoUserRepo

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

    @Test(priority = 0)
    open fun testFindAll() {
        var response = demoUserRepo.findAll()
        assertNotNull(response)
        assertEquals(false, response.isEmpty())
        assertEquals(100, response.size)
    }

    @Test(priority = 1)
    open fun testCreate() {
        val faker = Faker()
        var expected = DemoUserEntity()
        val now = OffsetDateTime.now()
        expected.firstName = faker.name().firstName()
        expected.lastName = faker.name().lastName()
        expected.city = faker.address().city()
        expected.state = faker.address().state()
        expected.zip = faker.address().zipCode()
        expected.address = faker.address().fullAddress()
        expected.favorites = arrayOf(
                faker.starTrek().specie(),
                faker.starTrek().specie(),
                faker.starTrek().specie())
        expected.avatar = faker.avatar().image()
        expected.createdDt = now
        expected.updatedDt = now
        expected.uuid = UUID.randomUUID()
        expected = demoUserRepo.save(expected)
        val actual = demoUserRepo.findByIdOrNull(expected.id)
        assertNotNull(actual)
        assertEntityEquality(expected, actual)
    }


    /**
     * Manually compare entity fields and assert equality.
     *
     * Although ReflectionEquals is more concise, it doesn't assert individual fields.
     */
    private fun assertEntityEquality(expected: DemoUserEntity, actual: DemoUserEntity) {
        assertEquals(expected.id, actual.id)
        assertEquals(expected.firstName, actual.firstName)
        assertEquals(expected.lastName, actual.lastName)
        assertEquals(expected.city, actual.city)
        assertEquals(expected.state, actual.state)
        assertEquals(expected.zip, actual.zip)
        assertEquals(expected.address, actual.address)
        assertEquals(expected.avatar, actual.avatar)
        assertEquals(expected.createdDt, actual.createdDt)
        assertEquals(expected.updatedDt, actual.updatedDt)
        assertEquals(expected.uuid, actual.uuid)
    }
}