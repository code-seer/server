package io.learnet.api.controller.user

import io.learnet.service.api.user.UserManagement
import io.learnet.web.api.*
import io.learnet.web.model.*
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.multipart.MultipartFile

@Controller
class UserController(
        private val userManagement: UserManagement
): PermissionsApi, ProfileApi, AvatarApi, SettingsApi, SocialApi {

    override fun createUserPermissions(): ResponseEntity<InlineResponse200> {
        val response = userManagement.createPermissions()
        return ResponseEntity.ok(InlineResponse200().status(response))
    }

    @PreAuthorize("hasRole(T(io.learnet.web.model.UserRole).READ_USER_PROFILE.value)")
    override fun getUserProfile(): ResponseEntity<UserProfileDto> {
        return ResponseEntity.ok(userManagement.getUserProfile())
    }

    @PreAuthorize("hasRole(T(io.learnet.web.model.UserRole).WRITE_USER_PROFILE.value)")
    override fun saveUserProfile(userProfileDto: UserProfileDto): ResponseEntity<UserProfileDto> {
        return ResponseEntity.ok(userManagement.saveUserProfile(userProfileDto))
    }

    @PreAuthorize("hasRole(T(io.learnet.web.model.UserRole).READ_USER_PROFILE.value)")
    override fun getAvatarUrl(): ResponseEntity<UserAvatarResponse> {
        return ResponseEntity.ok(userManagement.getAvatarUrl())
    }

    @PreAuthorize("hasRole(T(io.learnet.web.model.UserRole).WRITE_USER_PROFILE.value)")
    override fun uploadUserAvatar(avatar: MultipartFile): ResponseEntity<UserAvatarResponse> {
        return ResponseEntity.ok(userManagement.uploadUserAvatar(avatar))
    }

    override fun getUserLanguage(): ResponseEntity<UserLanguageDto> {
        return ResponseEntity.ok(userManagement.getUserLanguage())
    }

    override fun getUserNotificationSettings(): ResponseEntity<UserNotificationSettingsDto> {
        TODO("Not yet implemented")
    }

    override fun getUserPrivacySettings(): ResponseEntity<UserPrivacySettingsDto> {
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

    @PreAuthorize("hasRole(T(io.learnet.web.model.UserRole).READ_USER_PROFILE.value)")
    override fun getUserSocial(): ResponseEntity<UserSocialDto> {
        return ResponseEntity.ok(userManagement.getUserSocial())
    }

    @PreAuthorize("hasRole(T(io.learnet.web.model.UserRole).WRITE_USER_PROFILE.value)")
    override fun saveUserSocial(userSocialDto: UserSocialDto): ResponseEntity<UserSocialDto> {
        return ResponseEntity.ok(userManagement.saveUserSocial(userSocialDto))
    }
}
