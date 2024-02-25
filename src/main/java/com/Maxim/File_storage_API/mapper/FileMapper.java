package com.Maxim.File_storage_API.mapper;

import com.Maxim.File_storage_API.dto.FileDTO;
import com.Maxim.File_storage_API.entity.FileEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface FileMapper {
    FileDTO map(FileEntity userEntity);

    @InheritInverseConfiguration
    FileEntity map(FileDTO dto);
}