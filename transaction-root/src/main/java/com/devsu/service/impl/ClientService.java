package com.devsu.service.impl;

import com.devsu.dto.ClientDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {

    private final RestTemplate restTemplate;

    @CircuitBreaker(name = "clientService", fallbackMethod = "fallbackFindClientById")
    public ClientDTO findClientById(String clientId) {
        String url = "http://localhost:8080/api/v1/clientes/clientId/" + clientId;
        return restTemplate.getForObject(url, ClientDTO.class);
    }

    private ClientDTO fallbackFindClientById(String clientId, Throwable ex) {
        log.error("Error al obtener client {}. Causa: {}", clientId, ex.getMessage());
        return null;
    }
}
