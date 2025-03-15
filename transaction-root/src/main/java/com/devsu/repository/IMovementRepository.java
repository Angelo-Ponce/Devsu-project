package com.devsu.repository;

import com.devsu.model.Movement;

import java.time.LocalDateTime;
import java.util.List;

public interface IMovementRepository extends IGenericRepository<Movement, Long> {

    List<Movement> findMovementByAccount_PersonIdAndMovementDateBetween(Long accountPersonId, LocalDateTime movementDateAfter, LocalDateTime movementDateBefore);
}
