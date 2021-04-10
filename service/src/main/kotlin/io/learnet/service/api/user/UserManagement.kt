package io.learnet.service.api.user

import io.learnet.account.model.*
import org.springframework.web.multipart.MultipartFile

interface UserManagement {

    /**
     * Creates user permissions upon user login by updating the Firebase
     * user record object.
     */
    fun createPermissions(): String

    fun getUserProfile(): UserProfileDto

    fun getAvatarUrl(): UserAvatarResponse

    fun getUserLanguage(): UserLanguageDto

    fun getUserNotificationSettings(): UserNotificationSettingsDto

    fun saveUserPrivacySettings(): UserPrivacySettingsDto

    fun getUserSocial(): UserSocialDto

    fun saveUserProfile(userProfileDto: UserProfileDto): UserProfileDto

    fun uploadUserAvatar(avatar: MultipartFile): UserAvatarResponse

    fun saveUserLanguage(userLanguageDto: UserLanguageDto): UserLanguageDto

    fun saveUserNotificationSettings(userNotificationSettingsDto: UserNotificationSettingsDto):
            UserNotificationSettingsDto

    fun saveUserPrivacySettings(userPrivacySettingsDto: UserPrivacySettingsDto):
            UserPrivacySettingsDto

    fun saveUserSocial(userSocialDto: UserSocialDto): UserSocialDto
}