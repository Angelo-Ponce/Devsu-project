package com.devsu.service.impl;

import com.devsu.dto.AccountDTO;
import com.devsu.exception.ModelNotFoundException;
import com.devsu.mapper.AccountMapper;
import com.devsu.model.Account;
import com.devsu.repository.IAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private IAccountRepository repository;

    @InjectMocks
    private AccountServiceImpl service;

    private final String user = "Angelo";

    private final Long accountId = 1L;
    private Account mockAccount;
    private AccountDTO mockAccountDTO;

    @BeforeEach
    void setUp() {
        mockAccount = new Account();
        mockAccount.setAccountId(accountId);
        mockAccount.setStatus(Boolean.TRUE);

        mockAccountDTO = new AccountDTO();
        mockAccountDTO.setAccountId(accountId);
        mockAccountDTO.setStatus(Boolean.TRUE);
    }

    @Test
    void givenGetRepository_WhenCalled_ThenReturnCorrectRepositoryInstance() {
        assertEquals(repository, service.getRepository());
    }

    @Test
    void givenSave_ThenSuccessfullySaveTheEntity_WhenTheEntityHasData(){
        Account account = AccountMapper.INSTANCE.toAccount(mockAccountDTO);
        account.setCreatedByUser(user);
        when(repository.save(any(Account.class))).thenReturn(account);

        AccountDTO result = service.saveAccount(mockAccountDTO, user);
        assertNotNull(result);
        assertEquals(mockAccountDTO.getPersonId(), result.getPersonId());
        verify(repository, times(1)).save(any(Account.class));
    }

    @Test
    void givenUpdateEntity_WhenEntityExists_ThenReturnUpdatedEntity(){
        when(repository.findById(accountId)).thenReturn(Optional.of(mockAccount));
        when(repository.save(any(Account.class))).thenReturn(mockAccount);
        AccountDTO result = service.updateAccount(accountId, mockAccountDTO, user);
        assertNotNull(result);
        verify(repository, times(1)).findById(accountId);
        verify(repository, times(1)).save(any());
    }

    @Test
    void givenDeleteLogic_WhenClientExists_ThenMarkClientAsInactive() {
        when(repository.findById(accountId)).thenReturn(Optional.of(mockAccount));
        when(repository.save(mockAccount)).thenReturn(mockAccount);
        service.deleteLogic(accountId, user);
        assertFalse(mockAccount.getStatus());
        assertEquals(user, mockAccount.getLastModifiedByUser());
        verify(repository, times(1)).save(mockAccount);
        verify(repository, times(1)).save(mockAccount);
    }

    @Test
    void givenDeleteLogic_WhenClientDoesNotExist_ThenThrowModelNotFoundException() {
        when(repository.findById(accountId)).thenReturn(Optional.empty());
        assertThrows(
                ModelNotFoundException.class,
                () -> service.deleteLogic(accountId, user),
                "ID not found: " + accountId
        );
        verify(repository, never()).save(any());
    }

}