package io.learnet.account.service.api.user

import io.learnet.account.model.*
import org.springframework.web.multipart.MultipartFile

interface UserManagement {

    /**
     * Creates user permissions upon user login by updating the Firebase
     * user record object.
     */
    fun createPermissions(request: UserPermissionsRequest): String

    fun getUserProfile(email: String): UserProfileDto

    fun getAvatarUrl(email: String): UserAvatarResponse

    fun getUserLanguage(email: String): UserLanguageDto

    fun getUserNotificationSettings(email: String): UserNotificationSettingsDto

    fun saveUserPrivacySettings(email: String): UserPrivacySettingsDto

    fun getUserSocial(email: String): UserSocialDto

    fun saveUserProfile(userProfileDto: UserProfileDto): UserProfileDto

    fun uploadUserAvatar(avatar: MultipartFile): UserAvatarResponse

    fun saveUserLanguage(userLanguageDto: UserLanguageDto): UserLanguageDto

    fun saveUserNotificationSettings(userNotificationSettingsDto: UserNotificationSettingsDto):
            UserNotificationSettingsDto

    fun saveUserPrivacySettings(userPrivacySettingsDto: UserPrivacySettingsDto):
            UserPrivacySettingsDto

    fun saveUserSocial(userSocialDto: UserSocialDto): UserSocialDto









}