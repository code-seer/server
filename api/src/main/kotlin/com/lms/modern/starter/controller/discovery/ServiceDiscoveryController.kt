package com.lms.modern.starter.controller.discovery

import com.lms.modern.starter.api.DiscoveryApi
import com.lms.modern.starter.controller.mapper.ApiMapper
import com.lms.modern.starter.model.ServiceDiscoveryResponse
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller

@Controller
class ServiceDiscoveryController(
        private val discoveryClient: DiscoveryClient,
        private val apiMapper: ApiMapper
): DiscoveryApi {

    override fun getServiceInstances(applicationName: String?): ResponseEntity<ServiceDiscoveryResponse> {
        val response = ServiceDiscoveryResponse()
        response.instances = apiMapper.map(this.discoveryClient.getInstances(applicationName))
        return ResponseEntity.ok(response)
    }
}