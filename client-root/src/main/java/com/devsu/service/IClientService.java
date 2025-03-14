package com.devsu.service;

import com.devsu.dto.ClientDTO;
import com.devsu.model.Client;

public interface IClientService extends IGenericService<Client, Long> {

    ClientDTO save(ClientDTO clientDTO, String user);
    ClientDTO update(Long id, ClientDTO clientDTO, String user);
    void deleteLogic(Long id, String user);
}
