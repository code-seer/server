package com.lms.modern.starter.data.repo;

import com.lms.modern.starter.data.entity.DemoUserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class EntityMapper {
    public abstract DemoUserEntity map(DemoUserEntity source);
}