package io.learnet.account.api.controller.user

import io.learnet.account.api.*
import io.learnet.account.model.*
import io.learnet.account.service.api.user.UserManagement
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
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

    @PreAuthorize("hasRole(T(io.learnet.account.model.UserRole).READ_USER_PROFILE.value)")
    override fun getUserProfile(email: String): ResponseEntity<UserProfileDto> {
        return ResponseEntity.ok(userManagement.getUserProfile(email))
    }

    @PreAuthorize("hasRole(T(io.learnet.account.model.UserRole).WRITE_USER_PROFILE.value)")
    override fun saveUserProfile(userProfileDto: UserProfileDto): ResponseEntity<UserProfileDto> {
        return ResponseEntity.ok(userManagement.saveUserProfile(userProfileDto))
    }

    override fun getAvatarUrl(email: String): ResponseEntity<UserAvatarResponse> {
        return ResponseEntity.ok(userManagement.getAvatarUrl(email))
    }

    override fun uploadUserAvatar(avatar: MultipartFile, email: String): ResponseEntity<UserAvatarResponse> {
        return ResponseEntity.ok(userManagement.uploadUserAvatar(avatar, email))
    }

    override fun getUserLanguage(email: String): ResponseEntity<UserLanguageDto> {
        return ResponseEntity.ok(userManagement.getUserLanguage(email))
    }

    override fun getUserNotificationSettings(email: String?): ResponseEntity<UserNotificationSettingsDto> {
        TODO("Not yet implemented")
    }

    override fun getUserPrivacySettings(email: String?): ResponseEntity<UserPrivacySettingsDto> {
        TODO("Not yet implemented")
    }

    override fun saveUserLanguage(userLanguageDto: UserLanguageDto): ResponseEntity<UserLanguageDto> {
        return ResponseEntity.ok(userManagement.saveUserLanguage(userLanguageDto))
    }

    override fun saveUserNotificationSettings(userNotificationSettingsDto: UserNotificationSettingsDto?): ResponseEntity<UserNotificationSettingsDto> {
        TODO("Not yet implemented")
    }

    override fun saveUserPrivacySettings(userPrivacySettingsDto: UserPrivacySettingsDto?): ResponseEntity<UserPrivacySettingsDto> {
        TODO("Not yet implemented")
    }

    override fun getUserSocial(email: String): ResponseEntity<UserSocialDto> {
        return ResponseEntity.ok(userManagement.getUserSocial(email))
    }

    override fun saveUserSocial(userSocialDto: UserSocialDto): ResponseEntity<UserSocialDto> {
        return ResponseEntity.ok(userManagement.saveUserSocial(userSocialDto))
    }
}
