package com.Maxim.File_storage_API.mapper;

import com.Maxim.File_storage_API.dto.EventDTO;
import com.Maxim.File_storage_API.dto.FileDTO;
import com.Maxim.File_storage_API.entity.EventEntity;
import com.Maxim.File_storage_API.entity.FileEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface EventMapper {
    EventDTO map(EventEntity userEntity);

    @InheritInverseConfiguration
    EventEntity map(EventDTO dto);
}