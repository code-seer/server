package io.learnet.starter.api.controller.demo

import io.learnet.starter.api.DemoUserApi
import io.learnet.starter.api.controller.mapper.ApiMapper
import io.learnet.starter.model.DemoUserResponse
import io.learnet.starter.model.PageableRequest
import io.learnet.starter.service.api.demoUser.DemoUserService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller

@Controller
class DemoUserController(
        private val demoUserService: DemoUserService,
        private val apiMapper: ApiMapper
): DemoUserApi {

    @PreAuthorize("hasRole(T(io.learnet.starter.model.UserRole).DEMO_USER_READ.value)")
    override fun findAllUsers(pageableRequest: PageableRequest): ResponseEntity<DemoUserResponse> {
        val response = apiMapper.map(demoUserService.findAllUsers(pageableRequest))
        return ResponseEntity.ok(response)
    }
}
