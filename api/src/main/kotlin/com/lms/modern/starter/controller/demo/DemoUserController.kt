package com.lms.modern.starter.controller.demo

import com.lms.modern.starter.api.DemoUserApi
import com.lms.modern.starter.controller.mapper.ApiMapper
import com.lms.modern.starter.model.DemoUserResponse
import com.lms.modern.starter.model.PageableRequest
import com.lms.modern.starter.service.api.demoUser.DemoUserService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller

@Controller
class DemoUserController(
        private val demoUserService: DemoUserService,
        private val apiMapper: ApiMapper
): DemoUserApi {
    override fun findAllUsers(pageableRequest: PageableRequest): ResponseEntity<DemoUserResponse> {
        val response = apiMapper.map(demoUserService.findAllUsers(pageableRequest))
        return ResponseEntity.ok(response)
    }
}