package io.learnet.account.api.controller.user

import io.learnet.account.api.PermissionsApi
import io.learnet.account.model.InlineResponse200
import io.learnet.account.model.UserPermissionsRequest
import io.learnet.account.service.api.user.UserManagement
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller

@Controller
class UserController(
        private val userManagement: UserManagement
): PermissionsApi {

    override fun createUserPermissions(userPermissionsRequest: UserPermissionsRequest): ResponseEntity<InlineResponse200> {
        val response = userManagement.createPermissions(userPermissionsRequest)
        return ResponseEntity.ok(InlineResponse200().status(response))
    }
}
