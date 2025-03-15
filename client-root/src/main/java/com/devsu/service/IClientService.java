package com.devsu.service;

import com.devsu.dto.ClientDTO;
import com.devsu.model.Client;

import java.util.Optional;

public interface IClientService extends IGenericService<Client, Long> {

    Optional<ClientDTO> findClientByClientId(String clientId);
    ClientDTO save(ClientDTO clientDTO, String user);
    ClientDTO update(Long id, ClientDTO clientDTO, String user);
    void deleteLogic(Long id, String user);
}
