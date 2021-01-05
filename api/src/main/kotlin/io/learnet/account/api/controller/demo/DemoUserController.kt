package io.learnet.account.api.controller.demo

import io.learnet.account.api.DemoUserApi
import io.learnet.account.api.controller.mapper.ApiMapper
import io.learnet.account.model.DemoUserResponse
import io.learnet.account.model.PageableRequest
import io.learnet.account.service.api.demoUser.DemoUserService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller

@Controller
class DemoUserController(
        private val demoUserService: DemoUserService,
        private val apiMapper: ApiMapper
): DemoUserApi {

    @PreAuthorize("hasRole(T(io.learnet.account.model.UserRole).DEMO_USER_READ.value)")
    override fun findAllUsers(pageableRequest: PageableRequest): ResponseEntity<DemoUserResponse>? {
        val response = apiMapper.map(demoUserService.findAllUsers(pageableRequest))
        return response?.let { ResponseEntity.ok(it) }
    }
}
