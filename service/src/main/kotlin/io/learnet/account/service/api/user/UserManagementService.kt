package io.learnet.account.service.api.user

import com.google.firebase.auth.FirebaseAuth
import io.learnet.account.model.UserPermissionsRequest
import io.learnet.account.model.UserRole
import io.learnet.account.util.properties.UserPermissionsProps
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserManagementService: UserManagement {

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

        if (userRecord != null) {
            // Update the Firebase user. This should asynchronously update the user object on the client side
            FirebaseAuth.getInstance().setCustomUserClaims(userRecord.uid, claims as Map<String, Any>?)
        }
        return "OK"
    }
}