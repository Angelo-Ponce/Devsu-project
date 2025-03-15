package com.devsu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovementReportDTO {

    @JsonProperty("Fecha")
    private LocalDateTime movementDate;

    @JsonProperty("Cliente")
    private String name;

    @JsonProperty("NumeroCuenta")
    private String accountNumber;

    @JsonProperty("Tipo")
    private String accountType;

    @JsonProperty("SaldoInicial")
    private BigDecimal initialBalance;

    @JsonProperty("Estado")
    private Boolean movementStatus;

    @JsonProperty("Movimiento")
    private BigDecimal movementValue;

    @JsonProperty("SaldoDisponible")
    private BigDecimal balance;
}
