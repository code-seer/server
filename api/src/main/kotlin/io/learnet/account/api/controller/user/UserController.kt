package io.learnet.account.api.controller.user

import io.learnet.account.api.*
import io.learnet.account.model.*
import io.learnet.account.service.api.user.UserManagement
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.multipart.MultipartFile

@Controller
class UserController(
        private val userManagement: UserManagement
): PermissionsApi, ProfileApi, AvatarApi, SettingsApi, SocialApi {

    override fun createUserPermissions(userPermissionsRequest: UserPermissionsRequest): ResponseEntity<InlineResponse200> {
        val response = userManagement.createPermissions(userPermissionsRequest)
        return ResponseEntity.ok(InlineResponse200().status(response))
    }

    override fun getUserProfile(email: String?): ResponseEntity<UserProfileDto> {
        TODO("Not yet implemented")
    }

    override fun saveUserProfile(userProfileDto: UserProfileDto?): ResponseEntity<UserProfileDto> {
        TODO("Not yet implemented")
    }

    override fun getAvatarUrl(email: String?): ResponseEntity<UserAvatarResponse> {
        TODO("Not yet implemented")
    }

    override fun uploadUserAvatar(avatar: MultipartFile?): ResponseEntity<UserAvatarResponse> {
        TODO("Not yet implemented")
    }

    override fun getUserLanguage(email: String?): ResponseEntity<UserLanguageDto> {
        TODO("Not yet implemented")
    }

    override fun getUserNotificationSettings(email: String?): ResponseEntity<UserNotificationSettingsDto> {
        TODO("Not yet implemented")
    }

    override fun getUserPrivacySettings(email: String?): ResponseEntity<UserPrivacySettingsDto> {
        TODO("Not yet implemented")
    }

    override fun saveUserLanguage(userLanguageDto: UserLanguageDto?): ResponseEntity<UserLanguageDto> {
        TODO("Not yet implemented")
    }

    override fun saveUserNotificationSettings(userNotificationSettingsDto: UserNotificationSettingsDto?): ResponseEntity<UserNotificationSettingsDto> {
        TODO("Not yet implemented")
    }

    override fun saveUserPrivacySettings(userPrivacySettingsDto: UserPrivacySettingsDto?): ResponseEntity<UserPrivacySettingsDto> {
        TODO("Not yet implemented")
    }

    override fun getUserSocial(email: String?): ResponseEntity<UserSocialDto> {
        TODO("Not yet implemented")
    }

    override fun saveUserSocial(userSocialDto: UserSocialDto?): ResponseEntity<UserSocialDto> {
        TODO("Not yet implemented")
    }
}
