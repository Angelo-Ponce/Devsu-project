package com.devsu.service.impl;

import com.devsu.dto.ClientDTO;
import com.devsu.exception.ModelNotFoundException;
import com.devsu.model.Client;
import com.devsu.repository.IClientRepository;
import com.devsu.util.MapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private IClientRepository repository;

    @Mock
    private MapperUtil mapperUtil;

    @InjectMocks
    private ClientServiceImpl clientService;

    private final Long personId = 1L;
    private final String user = "Angelo";
    private Client mockClient;
    private ClientDTO mockClientDTO;

    @BeforeEach
    void setUp() {
        mockClient = new Client();
        mockClient.setPersonId(personId);
        mockClient.setIdentification("0912458963");
        mockClient.setName("Angelo");
        mockClient.setGender("Male");
        mockClient.setAge(20);
        mockClient.setAddress("Ecuador");
        mockClient.setPhone("0999");
        mockClient.setClientId("angelo");
        mockClient.setPassword("123456");
        mockClient.setStatus(true);

        mockClientDTO = new ClientDTO();
        mockClientDTO.setPersonId(personId);
        mockClientDTO.setIdentification("0912458963");
        mockClientDTO.setName("Angelo");
        mockClientDTO.setGender("Male");
        mockClientDTO.setAge(20);
        mockClientDTO.setAddress("Ecuador");
        mockClientDTO.setPhone("0999");
        mockClientDTO.setClientId("angelo");
        mockClientDTO.setPassword("123456");
        mockClientDTO.setStatus(true);
    }

    @Test
    void givenGetRepository_WhenCalled_ThenReturnCorrectRepositoryInstance() {
        assertEquals(repository, clientService.getRepository());
    }

    @Test
    void givenFindClientByClientId_WhenEntityExists_ThenReturnEntity(){
        when(mapperUtil.map(mockClient, ClientDTO.class)).thenReturn(mockClientDTO);
        when(repository.findClientByClientId(mockClient.getClientId())).thenReturn(Optional.of(mockClient));

        Optional<ClientDTO> result = clientService.findClientByClientId(mockClient.getClientId());
        assertEquals(mockClient.getClientId(), result.get().getClientId());
        verify(repository, times(1)).findClientByClientId(mockClient.getClientId());
    }

    @Test
    void givenSave_ThenSuccessfullySaveTheEntity_WhenTheEntityHasData(){
        when(mapperUtil.map(mockClientDTO, Client.class)).thenReturn(mockClient);
        when(repository.save(mockClient)).thenReturn(mockClient);
        when(mapperUtil.map(mockClient, ClientDTO.class)).thenReturn(mockClientDTO);

        ClientDTO result = clientService.save(mockClientDTO, user);
        assertNotNull(result);
        assertEquals(mockClient.getClientId(), result.getClientId());
        verify(repository, times(1)).save(mockClient);
    }

    @Test
    void givenUpdateEntity_WhenEntityExists_ThenReturnUpdatedEntity() {
        when(repository.findById(personId)).thenReturn(Optional.of(mockClient));
        when(repository.save(mockClient)).thenReturn(mockClient);
        when(mapperUtil.map(mockClient, ClientDTO.class)).thenReturn(mockClientDTO);

        ClientDTO result = clientService.update(personId, mockClientDTO, user);
        assertNotNull(result);
        verify(repository, times(1)).findById(personId);
        verify(repository, times(1)).save(mockClient);
    }

    @Test
    void givenDeleteLogic_WhenClientExists_ThenMarkClientAsInactive() {
        when(repository.findById(personId)).thenReturn(Optional.of(mockClient));

        clientService.deleteLogic(personId, user);

        assertEquals(Boolean.FALSE, mockClient.getStatus());
        assertEquals(user, mockClient.getLastModifiedByUser());
        verify(repository, times(1)).save(mockClient);
    }

    @Test
    void givenDeleteLogic_WhenClientDoesNotExist_ThenThrowModelNotFoundException() {
        when(repository.findById(personId)).thenReturn(Optional.empty());

        assertThrows(
                ModelNotFoundException.class,
                () -> clientService.deleteLogic(personId, user),
                "ID not found: " + personId
        );

        verify(repository, never()).save(any());
    }

}