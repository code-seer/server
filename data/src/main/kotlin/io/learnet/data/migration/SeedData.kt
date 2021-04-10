package io.learnet.data.migration

import com.github.javafaker.Faker
import io.learnet.data.entity.*
import io.learnet.data.repo.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.OffsetDateTime
import java.time.ZoneId
import java.util.*
import javax.annotation.PostConstruct

/**
 * Generates seed data used for demo and testing purposes. Data must not be generated
 * more than once. Otherwise, previous data would get overwritten and our integration
 * tests would fail. We would like to avoid this by hard-coding a flag in the code.
 */
@Component
class SeedData(
    private val userProfileRepo: UserProfileRepo,
    private val addressRepo: AddressRepo,
    private val socialRepo: SocialRepo,
    private val securityRepo: SecurityRepo,
    private val userSettingsRepo: UserSettingsRepo,
    private val flyway: FlywayMigration
) {

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
            seedUserProfile()
        }
    }

    private fun seedUserProfile() {
        log.info("Generating seed data for user_profile")
        val faker = Faker()
        for (i in 0 until rowcount) {
            val entity = UserProfileEntity()
            val now = OffsetDateTime.now()
            entity.address = getAddress()
            entity.social = getSocial()
            entity.security = getSecurity()
            entity.userSettings = getUserSettings()
            entity.firstName = faker.name().firstName()
            entity.lastName = faker.name().lastName()
            entity.middleName = faker.name().firstName()
            entity.title = faker.name().title()
            entity.gender = arrayListOf<String>("M", "F").random()
            entity.email = faker.internet().emailAddress()
            entity.isNewUser = true
            entity.homePhone = faker.phoneNumber().phoneNumber()
            entity.mobilePhone = faker.phoneNumber().cellPhone()
            entity.dateOfBirth = faker.date()
                    .birthday()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
            entity.avatar = faker.avatar().image()
            entity.createdDt = now
            entity.updatedDt = now
            entity.uuid = UUID.randomUUID()
            userProfileRepo.save(entity)
        }
    }

    private fun getUserSettings(): UserSettingsEntity? {
        val faker = Faker()
        val entity = UserSettingsEntity()
        val now = OffsetDateTime.now()
        entity.language = faker.starTrek().specie()
        entity.timezone = faker.address().timeZone()
        entity.createdDt = now
        entity.updatedDt = now
        entity.uuid = UUID.randomUUID()
        return userSettingsRepo.save(entity)

    }

    private fun getSecurity(): SecurityEntity? {
        val entity = SecurityEntity()
        val now = OffsetDateTime.now()
        entity.roles = arrayOf("ROLE_ADMIN", "ROLE_DEMO", "ROLE_USER")
        entity.createdDt = now
        entity.updatedDt = now
        entity.uuid = UUID.randomUUID()
        return securityRepo.save(entity)

    }

    private fun getSocial(): SocialEntity? {
        val faker = Faker()
        val entity = SocialEntity()
        val now = OffsetDateTime.now()
        entity.facebook = faker.internet().url()
        entity.twitter = faker.name().username()
        entity.linkedin = faker.internet().url()
        entity.github = faker.internet().url()
        entity.youtube = faker.internet().url()
        entity.instagram = faker.name().username()
        entity.snapchat = faker.name().username()
        entity.whatsapp = faker.phoneNumber().phoneNumber()
        entity.website = faker.internet().url()
        entity.createdDt = now
        entity.updatedDt = now
        entity.uuid = UUID.randomUUID()
        return socialRepo.save(entity)
    }

    private fun getAddress(): AddressEntity {
        val faker = Faker()
        val entity = AddressEntity()
        val now = OffsetDateTime.now()
        entity.country = faker.address().country()
        entity.state = faker.address().stateAbbr()
        entity.city = faker.address().city()
        entity.postalCode = faker.address().zipCode()
        entity.address1 = faker.address().fullAddress()
        entity.createdDt = now
        entity.updatedDt = now
        entity.uuid = UUID.randomUUID()
        return addressRepo.save(entity)
    }
}
