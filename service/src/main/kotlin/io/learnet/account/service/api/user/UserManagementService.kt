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
import org.springframework.util.ObjectUtils


@Service
class UserManagementService(
    private val s3: S3,
    private val s3Props: S3Props,
    private val userProfileRepo: UserProfileRepo,
    private val socialRepo: SocialRepo,
    private val securityRepo: SecurityRepo,
    private val userSettingsRepo: UserSettingsRepo,
    private val addressRepo: AddressRepo,
    private val userPrincipal: UserDto): UserManagement {

    @Autowired
    lateinit var userPermissions: UserPermissionsProps

    override fun createPermissions(): String {
        val claims =  hashMapOf<String, Boolean>()
        val userRecord = FirebaseAuth.getInstance().getUserByEmail(userPrincipal.email)

        // A user is authorized to use the UI if explicitly permitted (only applicable during dev)
        var isUiAuthorized = false
        userPermissions.uiAuthorizedEmails.iterator().forEach {
            if (it == userPrincipal.email) {
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

    override fun getUserProfile(): UserProfileDto {
        return mapEntityToDto(userProfileRepo.findByEmail(userPrincipal.email))
    }

    override fun getAvatarUrl(): UserAvatarResponse {
        val entity = userProfileRepo.findByEmail(userPrincipal.email)
        val response = UserAvatarResponse()
        if (entity != null) {
            response.url = entity.avatar
        }
        return response
    }

    override fun getUserLanguage(): UserLanguageDto {
        val entity = getUserProfileEntity(userPrincipal.email)
        val response = UserLanguageDto()
        if (entity.language != null) {
            response.language = entity.language
        }
        return response
    }

    override fun getUserNotificationSettings(): UserNotificationSettingsDto {
        TODO("Not yet implemented")
    }

    override fun saveUserPrivacySettings(): UserPrivacySettingsDto {
        TODO("Not yet implemented")
    }

    override fun saveUserPrivacySettings(userPrivacySettingsDto: UserPrivacySettingsDto): UserPrivacySettingsDto {
        TODO("Not yet implemented")
    }

    override fun getUserSocial(): UserSocialDto {
        val entity = getUserProfileEntity(userPrincipal.email)
        val dto = UserSocialDto()
        if (entity?.social != null) {
            dto.facebook = entity.social!!.facebook
            dto.github = entity.social!!.github
            dto.instagram = entity.social!!.instagram
            dto.whatsapp = entity.social!!.whatsapp
            dto.twitter = entity.social!!.twitter
            dto.linkedin = entity.social!!.linkedin
            dto.website = entity.social!!.website
        }
        return dto
    }

    override fun saveUserProfile(userProfileDto: UserProfileDto): UserProfileDto {
        val now = OffsetDateTime.now()
        var entity = getUserProfileEntity(userPrincipal.email)
        entity.email = userPrincipal.email
        var addressEntity = entity.address
        if (!ObjectUtils.isEmpty(userProfileDto.firstName)) {
            entity.firstName = userProfileDto.firstName
        }
        if (!ObjectUtils.isEmpty(userProfileDto.lastName)) {
            entity.lastName = userProfileDto.lastName
        }
        if (!ObjectUtils.isEmpty(userProfileDto.title)) {
            entity.title = userProfileDto.title
        }
        if (!ObjectUtils.isEmpty(userProfileDto.mobilePhone)) {
            entity.mobilePhone = userProfileDto.mobilePhone
        }
        if (!ObjectUtils.isEmpty(userProfileDto.homePhone)) {
            entity.homePhone = userProfileDto.homePhone
        }
        if (!ObjectUtils.isEmpty(userProfileDto.email)) {
            entity.secondaryEmail = userProfileDto.email
        }
        if (addressEntity == null) {
            addressEntity = AddressEntity()
            addressEntity.createdDt = now
            addressEntity.uuid = UUID.randomUUID()
        }
        if (!ObjectUtils.isEmpty(userProfileDto.address)) {
            addressEntity.address1 = userProfileDto.address
        }
        if (!ObjectUtils.isEmpty(userProfileDto.city)) {
            addressEntity.city = userProfileDto.city
        }
        if (!ObjectUtils.isEmpty(userProfileDto.state)) {
            addressEntity.state = userProfileDto.state
        }
        if (!ObjectUtils.isEmpty(userProfileDto.postalCode)) {
            addressEntity.postalCode = userProfileDto.postalCode
        }
        if (!ObjectUtils.isEmpty(userProfileDto.country)) {
            addressEntity.country = userProfileDto.country
        }
        addressEntity.updatedDt = now
        addressRepo.save(addressEntity)
        entity.address = addressEntity
        entity.updatedDt = now
        userProfileRepo.save(entity)
        return mapEntityToDto(entity)
    }

    override fun uploadUserAvatar(avatar: MultipartFile): UserAvatarResponse {
        val newObjectKey = UUID.randomUUID().toString()
        val entity = getUserProfileEntity(userPrincipal.email)
        entity.avatarObjectKey?.let { s3.deleteObject(s3Props.bucket, it) }
        val url = s3.uploadObject(s3Props.bucket, newObjectKey, avatar)
        entity.email = userPrincipal.email
        entity.avatar = url
        entity.avatarObjectKey = newObjectKey
        entity.updatedDt = OffsetDateTime.now()
        userProfileRepo.save(entity)
        val response = UserAvatarResponse()
        response.url = url
        return response
    }

    override fun saveUserLanguage(userLanguageDto: UserLanguageDto): UserLanguageDto {
        val entity = getUserProfileEntity(userPrincipal.email)
        entity.language = userLanguageDto.language
        userProfileRepo.save(entity)
        return userLanguageDto
    }

    override fun saveUserNotificationSettings(userNotificationSettingsDto: UserNotificationSettingsDto): UserNotificationSettingsDto {
        TODO("Not yet implemented")
    }

    override fun saveUserSocial(userSocialDto: UserSocialDto): UserSocialDto {
        val entity = getUserProfileEntity(userPrincipal.email)
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
        var entity = userProfileRepo.findByEmail(userPrincipal.email)
        if (entity == null) {
            entity = UserProfileEntity()
            entity.email = email
            entity.createdDt = OffsetDateTime.now()
            entity.uuid = UUID.randomUUID()
        }
        return entity
    }

    private fun mapEntityToDto(entity: UserProfileEntity?): UserProfileDto {
        val dto = UserProfileDto()
        dto.avatar = entity?.avatar
        dto.title = entity?.title
        dto.firstName = entity?.firstName
        dto.lastName = entity?.lastName
        dto.email = entity?.secondaryEmail
        dto.mobilePhone = entity?.mobilePhone
        dto.homePhone = entity?.homePhone
        dto.address = entity?.address?.address1
        dto.country = entity?.address?.country
        dto.state = entity?.address?.state
        dto.city = entity?.address?.city
        dto.postalCode = entity?.address?.postalCode
        return dto
    }
}