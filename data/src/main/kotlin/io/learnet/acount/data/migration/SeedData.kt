package io.learnet.acount.data.migration

import com.github.javafaker.Faker
import io.learnet.acount.data.entity.DemoUserEntity
import io.learnet.acount.data.repo.DemoUserRepo
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.OffsetDateTime
import java.util.*
import javax.annotation.PostConstruct

/**
 * Generates seed data used for demo and testing purposes. Data must not be generated
 * more than once. Otherwise, previous data would get overwritten and our integration
 * tests would fail. We would like to avoid this by hard-coding a flag in the code.
 */
@Component
class SeedData(private val demoUserRepo: DemoUserRepo, private val flyway: FlywayMigration) {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)
    private val rowcount = 100

    //=======================================================================
    //                      DANGER
    // Set generateDate to true only if you want to overwrite the seed data.
    // This operation is dangerous as it would most likely break all integration
    // tests.
    //=======================================================================
    private val generateData = false

    @PostConstruct
    fun seed() {
        if (generateData) {
            flyway.clean()
            flyway.init()
            log.info("Generating seed data")
            seedDemoUser()
        }
    }

    private fun seedDemoUser() {
        log.info("Generating seed data for demo_user")
        val faker = Faker()
        for (i in 0 until rowcount) {
            val entity = DemoUserEntity()
            val now = OffsetDateTime.now()
            entity.firstName = faker.name().firstName()
            entity.lastName = faker.name().lastName()
            entity.city = faker.address().city()
            entity.state = faker.address().state()
            entity.zip = faker.address().zipCode()
            entity.address = faker.address().fullAddress()
            entity.favorites = arrayOf(
                    faker.starTrek().specie(),
                    faker.starTrek().specie(),
                    faker.starTrek().specie())
            entity.avatar = faker.avatar().image()
            entity.createdDt = now
            entity.updatedDt = now
            entity.uuid = UUID.randomUUID()
            demoUserRepo.save(entity)
        }
    }
}
