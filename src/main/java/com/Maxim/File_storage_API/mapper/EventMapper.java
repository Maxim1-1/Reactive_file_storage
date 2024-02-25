package com.Maxim.File_storage_API.mapper;

import com.Maxim.File_storage_API.dto.EventDTO;
import com.Maxim.File_storage_API.entity.EventEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface EventMapper {
    EventDTO map(EventEntity eventEntity);

    @InheritInverseConfiguration
    EventEntity map(EventDTO eventDTO);
}