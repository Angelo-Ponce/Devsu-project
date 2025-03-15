package com.devsu.repository;

import com.devsu.model.Client;

import java.util.Optional;

public interface IClientRepository extends IGenericRepository<Client, Long> {

    Optional<Client> findClientByClientId(String clientId);
}
