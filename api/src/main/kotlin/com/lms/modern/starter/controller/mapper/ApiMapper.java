package com.lms.modern.starter.controller.mapper;

import com.lms.modern.starter.model.DemoUserDto;
import com.lms.modern.starter.model.DemoUserResponse;
import com.lms.modern.starter.model.ServiceInstanceDto;
import com.lms.modern.starter.search.api.LmsPage;
import org.jetbrains.annotations.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy =  ReportingPolicy.IGNORE
)
public abstract class ApiMapper {

    public abstract List<ServiceInstanceDto> map(@NotNull List<? extends ServiceInstance> instances);

    @Mapping(target = "isSecure", expression = "java(serviceInstance.isSecure())")
    public abstract ServiceInstanceDto map(ServiceInstance serviceInstance);

    @Mapping(source = "records", target = "content")
    public abstract DemoUserResponse map(LmsPage<DemoUserDto> page);
}
