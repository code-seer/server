package io.learnet.util.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("user.permissions")
class UserPermissionsProps {
    lateinit var uiAuthorizedEmails: Array<String>
    lateinit var defaultPermissions: Array<String>
}
