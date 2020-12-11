package io.learnet.account.service.api.user

import io.learnet.account.model.UserPermissionsRequest

interface UserManagement {

    /**
     * Creates user permissions upon user login by updating the Firebase
     * user record object.
     */
    fun createPermissions(request: UserPermissionsRequest): String

}