package com.devsu.mapper;

import com.devsu.dto.MovementDTO;
import com.devsu.model.Movement;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MovementMapper {

    MovementMapper INSTANCE = Mappers.getMapper(MovementMapper.class);

    MovementDTO toMovementDTO(Movement movement);

    Movement toMovement(MovementDTO movementDTO);
}
