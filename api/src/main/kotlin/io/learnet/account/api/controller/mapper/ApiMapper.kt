package io.learnet.starter.api.controller.mapper

import io.learnet.starter.model.DemoUserDto
import io.learnet.starter.model.DemoUserResponse
import io.learnet.starter.model.ServiceInstanceDto
import io.learnet.starter.search.api.LmsPage
import org.springframework.cloud.client.ServiceInstance
import org.springframework.stereotype.Component
import java.util.ArrayList

@Component
class ApiMapper {
    fun map(instances: List<ServiceInstance?>?): List<ServiceInstanceDto?>? {
        if (instances == null) {
            return null
        }
        val list: MutableList<ServiceInstanceDto?> = ArrayList(instances.size)
        for (serviceInstance in instances) {
            list.add(map(serviceInstance))
        }
        return list
    }

    fun map(serviceInstance: ServiceInstance?): ServiceInstanceDto? {
        if (serviceInstance == null) {
            return null
        }
        val serviceInstanceDto = ServiceInstanceDto()
        serviceInstanceDto.instanceId = serviceInstance.instanceId
        serviceInstanceDto.serviceId = serviceInstance.serviceId
        serviceInstanceDto.host = serviceInstance.host
        serviceInstanceDto.port = serviceInstance.port
        serviceInstanceDto.uri = serviceInstance.uri
        serviceInstanceDto.metadata = serviceInstance.metadata
        serviceInstanceDto.scheme = serviceInstance.scheme
        serviceInstanceDto.isSecure = serviceInstance.isSecure
        return serviceInstanceDto
    }

    fun map(page: LmsPage<DemoUserDto>?): DemoUserResponse? {
        if (page == null) {
            return null
        }
        val demoUserResponse = DemoUserResponse()
        val list: List<DemoUserDto> = page.records
        if (list != null) {
            demoUserResponse.content = ArrayList(list)
        }
        demoUserResponse.limit = page.limit
        demoUserResponse.offset = page.offset
        demoUserResponse.totalRecords = page.totalRecords
        demoUserResponse.numPages = page.numPages
        return demoUserResponse
    }
}