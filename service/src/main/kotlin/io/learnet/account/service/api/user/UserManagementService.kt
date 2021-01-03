package io.learnet.account.service.api.user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserRecord
import io.learnet.account.data.entity.AddressEntity
import io.learnet.account.data.entity.SocialEntity
import io.learnet.account.data.entity.UserProfileEntity
import io.learnet.account.data.repo.*
import io.learnet.account.model.*
import io.learnet.account.service.api.aws.S3
import io.learnet.account.util.properties.S3Props
import io.learnet.account.util.properties.UserPermissionsProps
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.OffsetDateTime
import java.util.*
import kotlin.collections.HashMap

@Service
class UserManagementService(
    private val s3: S3,
    private val s3Props: S3Props,
    private val userProfileRepo: UserProfileRepo,
    private val socialRepo: SocialRepo,
    private val securityRepo: SecurityRepo,
    private val userSettingsRepo: UserSettingsRepo,
    private val addressRepo: AddressRepo): UserManagement {

    @Autowired
    lateinit var userPermissions: UserPermissionsProps

    override fun createPermissions(request: UserPermissionsRequest): String {
        val claims =  hashMapOf<String, Boolean>()
        val userRecord = FirebaseAuth.getInstance().getUserByEmail(request.email)

        // A user is authorized to use the UI if explicitly permitted (only applicable during dev)
        var isUiAuthorized = false
        userPermissions.uiAuthorizedEmails.iterator().forEach {
            if (it == request.email) {
                isUiAuthorized = true
        }}
        if (isUiAuthorized) {
            claims[UserRole.VIEW_DASHBOARD.value] = true
        }

        // Apply default permissions
        userPermissions.defaultPermissions.iterator().forEach {
            claims[it] = true
        }

        // A user may also have roles specific to them
        // TODO: load roles from db
        // val userSpecificRoles = load from db

        var status = "OK"
        if (userRecord != null) {
            if (doUpdateRoles(claims, userRecord)) {
                // Update the Firebase user. This should asynchronously update the user object on the client side
                FirebaseAuth.getInstance().setCustomUserClaims(userRecord.uid, claims as Map<String, Any>?)
            } else {
                status = "No Change"
            }
        }
        return status
    }

    override fun getUserProfile(email: String): UserProfileDto {
        TODO("Not yet implemented")
    }

    override fun getAvatarUrl(email: String): UserAvatarResponse {
        val entity = userProfileRepo.findByEmail(email)
        val response = UserAvatarResponse()
        if (entity != null) {
            response.url = entity.avatar
        }
        return response
    }

    override fun getUserLanguage(email: String): UserLanguageDto {
        var entity = getUserProfileEntity(email)
        val response = UserLanguageDto()
        if (entity.language != null) {
            response.language = entity.language
        }
        return response
    }

    override fun getUserNotificationSettings(email: String): UserNotificationSettingsDto {
        TODO("Not yet implemented")
    }

    override fun saveUserPrivacySettings(email: String): UserPrivacySettingsDto {
        TODO("Not yet implemented")
    }

    override fun saveUserPrivacySettings(userPrivacySettingsDto: UserPrivacySettingsDto): UserPrivacySettingsDto {
        TODO("Not yet implemented")
    }

    override fun getUserSocial(email: String): UserSocialDto {
        TODO("Not yet implemented")
    }

    override fun saveUserProfile(userProfileDto: UserProfileDto): UserProfileDto {
        val now = OffsetDateTime.now()
        val entity = getUserProfileEntity(userProfileDto.email)
        var addressEntity = entity.address
        entity.firstName = userProfileDto.firstName
        entity.lastName = userProfileDto.lastName
        entity.title = userProfileDto.title
        entity.mobilePhone = userProfileDto.mobilePhone
        entity.homePhone = userProfileDto.homePhone
        entity.email = userProfileDto.email
        if (addressEntity == null) {
            addressEntity = AddressEntity()
            addressEntity.createdDt = now
            addressEntity.uuid = UUID.randomUUID()
        }
        addressEntity.address1 = userProfileDto.address
        addressEntity.city = userProfileDto.city
        addressEntity.state = userProfileDto.state
        addressEntity.postalCode = userProfileDto.postalCode
        addressEntity.country = userProfileDto.country
        addressEntity.updatedDt = now
        addressRepo.save(addressEntity)
        entity.address = addressEntity
        entity.updatedDt = now
        entity.isNewUser = userProfileDto.isNewUser
        userProfileRepo.save(entity)
        return userProfileDto
    }

    override fun uploadUserAvatar(avatar: MultipartFile, email: String): UserAvatarResponse {
        val newObjectKey = UUID.randomUUID().toString()
        val entity = getUserProfileEntity(email)
        entity.avatarObjectKey?.let { s3.deleteObject(s3Props.bucket, it) }
        val url = s3.uploadObject(s3Props.bucket, newObjectKey, avatar)
        entity.email = email
        entity.avatar = url
        entity.avatarObjectKey = newObjectKey
        entity.updatedDt = OffsetDateTime.now()
        userProfileRepo.save(entity)
        val response = UserAvatarResponse()
        response.url = url
        return response
    }

    override fun saveUserLanguage(userLanguageDto: UserLanguageDto): UserLanguageDto {
        val entity = getUserProfileEntity(userLanguageDto.email)
        entity.language = userLanguageDto.language
        userProfileRepo.save(entity)
        return userLanguageDto
    }

    override fun saveUserNotificationSettings(userNotificationSettingsDto: UserNotificationSettingsDto): UserNotificationSettingsDto {
        TODO("Not yet implemented")
    }

    override fun saveUserSocial(userSocialDto: UserSocialDto): UserSocialDto {
        val entity = getUserProfileEntity(userSocialDto.email)
        var social = entity.social
        val now = OffsetDateTime.now()
        if (social == null) {
            social = SocialEntity()
            social.uuid = UUID.randomUUID();
            social.createdDt = now
        }
        social.facebook = userSocialDto.facebook
        social.twitter = userSocialDto.twitter
        social.github = userSocialDto.github
        social.instagram = userSocialDto.instagram
        social.website = userSocialDto.website
        social.whatsapp = userSocialDto.whatsapp
        social.linkedin = userSocialDto.linkedin
        social.updatedDt = now
        socialRepo.save(social)
        entity.social = social
        userProfileRepo.save(entity)
        return userSocialDto
    }

    /**
     * Return true if the number of claims in Firebase is not equal to the new claims
     */
    private fun doUpdateRoles(newClaims: HashMap<String, Boolean>, userRecord: UserRecord): Boolean {
        val fireBaseClaims =  userRecord.customClaims.toList()
        if (newClaims.size != fireBaseClaims.size) {
            return true
        }
        var matches = 0
        for (claim in fireBaseClaims) {
            if (newClaims.containsKey(claim.first)) {
                matches++
            }
        }
        return matches != fireBaseClaims.size
    }

    private fun getUserProfileEntity(email: String): UserProfileEntity {
        var entity = userProfileRepo.findByEmail(email)
        if (entity == null) {
            entity = UserProfileEntity()
            entity.email = email
            entity.createdDt = OffsetDateTime.now()
            entity.uuid = UUID.randomUUID()
        }
        return entity
    }
}