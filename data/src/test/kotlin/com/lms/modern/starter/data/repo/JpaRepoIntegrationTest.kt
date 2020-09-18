package com.lms.modern.starter.data.repo

import com.lms.modern.starter.config.SystemConfigConfiguration
import com.lms.modern.starter.data.DataTestConfiguration
import com.lms.modern.starter.data.entity.DemoUserEntity
import com.lms.modern.starter.data.migration.FlywayMigration
import org.junit.Test
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.junit.runner.RunWith
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional
import java.lang.reflect.Method
import java.time.OffsetDateTime
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@RunWith(SpringRunner::class)
@SpringBootTest(classes = [DataTestConfiguration::class, SystemConfigConfiguration::class])
open class JpaRepoIntegrationTest {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    lateinit var demoUserRepo: DemoUserRepo

    @Autowired
    lateinit var flywayMigration: FlywayMigration

//    @Autowired
//    lateinit var entityMapper: EntityMapper

    @BeforeAll
    fun beforeClass() {
        flywayMigration.clean()
        flywayMigration.init()
    }

    @AfterAll
    open fun afterClass() {
        flywayMigration.clean()
    }

    @BeforeEach
    open fun beforeMethod(method: Method) {
        log.info("Testing ${method.name}")
    }

    @Test
    open fun testCreate() {
        flywayMigration.clean()
        flywayMigration.init()
//        val expected = getEntity()
//        val saved = demoUserRepo.save(expected)

        // Save multiple copies to test that transactions are rolled back. If rollback fails,
        // testFindAll() should fail
//        demoUserRepo.save(getEntity())
//        demoUserRepo.save(getEntity())
//        demoUserRepo.save(getEntity())
//        demoUserRepo.save(getEntity())
//
//        val response = saved.id?.let { demoUserRepo.findById(it) }
//        assertNotNull(response)
//        assertEquals(false, response.isEmpty)
//        val actual = response.get()
//        assertEntityEquality(expected, actual)
    }

//    @Test
//    open fun testUpdate() {
//        // Create a new entity and save it
//        val expected = getEntity()
//        val saved = demoUserRepo.save(expected)
//        var response = saved.id?.let { demoUserRepo.findById(it) }
//        assertNotNull(response)
//        assertEquals(false, response.isEmpty)
//        val actual = response.get()
//        assertEntityEquality(expected, actual)
//
//        // Update the entity
//        val toUpdate = entityMapper.map(actual)
//        toUpdate.status = "Active"
//        toUpdate.location = "Chicago"
//        toUpdate.updatedDt = OffsetDateTime.now()
//        demoUserRepo.save(toUpdate)
//        response = saved.id?.let { demoUserRepo.findById(it) }
//        assertNotNull(response)
//        assertEquals(false, response.isEmpty)
//        val updated = response.get()
//        assertEntityEquality(toUpdate, updated)
//    }

//    @Test
//    open fun testDelete() {
//        val expected = getEntity()
//        val saved = demoUserRepo.save(expected)
//        var response = saved.id?.let { demoUserRepo.findById(it) }
//        assertNotNull(response)
//        assertEquals(false, response.isEmpty)
//        var actual = response.get()
//        assertEntityEquality(expected, actual)
//
//        // Delete the entity
//        demoUserRepo.delete(actual)
//        response = saved.id?.let { demoUserRepo.findById(it) }
//        assertNotNull(response)
//        assertEquals(response.isEmpty, true)
//    }

//    @Test
//    open fun testFindById() {
//        var response = demoUserRepo.findById(1)
//        assertNotNull(response)
//        assertEquals(false, response.isEmpty)
//        var actual = response.get()
//        assertEquals("Gamification", actual.name)
//        assertEquals("Absolute Beginner", actual.level)
//        assertEquals("G_oY", actual.shortName)
//        assertEquals(399, actual.price)
//        assertEquals("Contest slippered insular central merged", actual.learningObjectives[4])
//    }

    @Test
    open fun testFindAll() {
        var response = demoUserRepo.findAll()
        assertNotNull(response)
        assertEquals(false, response.isEmpty())
        assertEquals(606, response.size)
    }

    private fun getEntity(): DemoUserEntity {
        val requirements = arrayOf("Alpha req", "Beta req", "Gamma req")
        val learningObjectives = arrayOf("Alpha objective", "Beta objective", "Gamma objective")
        val skills = arrayOf("Alpha skill", "Beta skill", "Gamma skill")
        val entity = DemoUserEntity()
        val now = OffsetDateTime.now()
//        entity.id = null
//        entity.status = "Draft"
//        entity.level = "Introductory"
//        entity.subject = "Computer Science,Root.Engineering.Computer_Science"
//        entity.instructionMode = "Offline"
//        entity.name = "Spring Boot Test course"
//        entity.description = "This is a test course created for testing Spring Data integration"
//        entity.requirements = requirements
//        entity.location =  "Berlin"
//        entity.numCredits = 4
//        entity.price = 99
//        entity.weeklyEffort = 8
//        entity.avatar = "avatar not available"
//        entity.learningObjectives = learningObjectives
//        entity.shortName = "SBTC"
//        entity.skills = skills
        entity.createdDt = now
        entity.updatedDt = now
        entity.uuid = UUID.randomUUID()
        return entity
    }
//
//    /**
//     * Manually compare entity fields and assert equality.
//     *
//     * Although ReflectionEquals is more concise, it doesn't assert individual fields.
//     */
//    private fun assertEntityEquality(expected: DemoEntity, actual: DemoEntity) {
//        assertEquals(expected.id, actual.id)
//        assertEquals(expected.status, actual.status)
//        assertEquals(expected.level, actual.level)
//        assertEquals(expected.subject, actual.subject)
//        assertEquals(expected.instructionMode, actual.instructionMode)
//        assertEquals(expected.name, actual.name)
//        assertEquals(expected.description, actual.description)
//        assertEquals(expected.requirements contentEquals actual.requirements, true)
//        assertEquals(expected.location, actual.location)
//        assertEquals(expected.numCredits, actual.numCredits)
//        assertEquals(expected.price, actual.price)
//        assertEquals(expected.weeklyEffort, actual.weeklyEffort)
//        assertEquals(expected.avatar, actual.avatar)
//        assertEquals(expected.learningObjectives contentEquals actual.learningObjectives, true)
//        assertEquals(expected.shortName, actual.shortName)
//        assertEquals(expected.skills contentEquals actual.skills, true)
//        assertEquals(expected.startTimeDt, actual.startTimeDt)
//        assertEquals(expected.endTimeDt, actual.endTimeDt)
//        assertEquals(expected.createdDt, actual.createdDt)
//        assertEquals(expected.updatedDt, actual.updatedDt)
//        assertEquals(expected.uuid, actual.uuid)
//    }
}