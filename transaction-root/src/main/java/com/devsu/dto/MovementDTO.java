package com.devsu.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovementDTO {

    private Long movementId;

    @NotNull(message = "{account.id.empty}")
    private Long accountId;

    private LocalDateTime movementDate;

    private String movementType;

    @NotNull(message = "{movement.value}")
    private BigDecimal movementValue;

    private BigDecimal balance;

    private Boolean status;
}
