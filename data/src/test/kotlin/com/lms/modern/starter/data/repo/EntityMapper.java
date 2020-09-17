package com.lms.modern.starter.data.repo;

import com.lms.modern.starter.data.entity.DemoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class EntityMapper {
    public abstract DemoEntity map(DemoEntity source);
}