package com.khokhlov.tendermonitoring.mapper;

import com.khokhlov.tendermonitoring.model.dto.UserCreateDTO;
import com.khokhlov.tendermonitoring.model.dto.UserDTO;
import com.khokhlov.tendermonitoring.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserDTO toDTO(User user);

    User toEntity(UserCreateDTO createDTO);
}
