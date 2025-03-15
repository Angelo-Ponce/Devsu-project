package com.devsu.service.impl;

import com.devsu.dto.ClientDTO;
import com.devsu.exception.ModelNotFoundException;
import com.devsu.model.Client;
import com.devsu.repository.IClientRepository;
import com.devsu.repository.IGenericRepository;
import com.devsu.service.IClientService;
import com.devsu.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl extends GenericServiceImpl<Client, Long> implements IClientService {

    private final IClientRepository repository;

    private final MapperUtil mapperUtil;

    @Override
    protected IGenericRepository<Client, Long> getRepository() {
        return repository;
    }

    @Override
    public Optional<ClientDTO> findClientByClientId(String clientId) {
        return repository.findClientByClientId(clientId)
                .map( client ->  mapperUtil.map(client, ClientDTO.class));
    }

    @Override
    public ClientDTO save(ClientDTO clientDTO, String user) {
        Client client = mapperUtil.map(clientDTO, Client.class);
        client.setCreatedByUser(user);
        return mapperUtil.map(repository.save(client), ClientDTO.class);
    }

    @Override
    public ClientDTO update(Long id, ClientDTO clientDTO, String user) {
        Client clientEntity = repository.findById(id)
                .map(client -> {
                    client.setIdentification(clientDTO.getIdentification());
                    client.setName(clientDTO.getName());
                    client.setGender(clientDTO.getGender());
                    client.setAge(clientDTO.getAge());
                    client.setAddress(clientDTO.getAddress());
                    client.setPhone(clientDTO.getPhone());
                    client.setClientId(clientDTO.getClientId());
                    client.setPassword(clientDTO.getPassword());
                    client.setStatus(clientDTO.getStatus());
                    client.setLastModifiedByUser(user);
                    return repository.save(client);
                }).orElseThrow(() -> new ModelNotFoundException("ID not found: " + id));
        return mapperUtil.map(clientEntity, ClientDTO.class);
    }

    @Override
    public void deleteLogic(Long id, String user) {
        Client client = this.findById(id);
        client.setStatus(Boolean.FALSE);
        client.setLastModifiedByUser(user);
        repository.save(client);
    }
}
