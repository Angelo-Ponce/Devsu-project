package com.devsu.service;

import com.devsu.dto.MovementDTO;
import com.devsu.dto.MovementReportDTO;
import com.devsu.model.Movement;

import java.time.LocalDateTime;
import java.util.List;

public interface IMovementService extends IGenericService<Movement, Long> {

    MovementDTO saveMovement(MovementDTO movementDTO, String user);
    MovementDTO updateMovement(Long id, MovementDTO movementDTO, String user);
    void deleteLogic(Long id, String user);

    List<MovementReportDTO> reportMovementByDateAndClientId(String clientId, LocalDateTime startDate, LocalDateTime endDate);
}
