package com.Maxim.File_storage_API.mapper;

import com.Maxim.File_storage_API.dto.UserDTO;
import com.Maxim.File_storage_API.entity.UserEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO map(UserEntity userEntity);

    @InheritInverseConfiguration
    UserEntity map(UserDTO dto);
}