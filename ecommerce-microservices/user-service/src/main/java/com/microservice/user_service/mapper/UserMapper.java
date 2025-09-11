package com.microservice.user_service.mapper;

import com.microservice.user_service.domain.dto.AuthUserResponseDto;
import com.microservice.user_service.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    AuthUserResponseDto toResponseDto(User user);
}
