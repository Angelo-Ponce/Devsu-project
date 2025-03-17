package com.devsu.service.impl;

import com.devsu.dto.AccountDTO;
import com.devsu.dto.MovementDTO;
import com.devsu.exception.ModelNotFoundException;
import com.devsu.exception.MovementException;
import com.devsu.model.Account;
import com.devsu.model.Movement;
import com.devsu.repository.IMovementRepository;
import com.devsu.service.IAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovementServiceImplTest {

    @Mock
    private IMovementRepository repository;

    @Mock
    private IAccountService accountService;

    @InjectMocks
    private MovementServiceImpl service;

    private final String user = "Angelo";

    private final Long movementId = 1L;

    private Movement mockMovement;
    private MovementDTO mockMovementDTO;

    @BeforeEach
    void setUp() {
        mockMovement = new Movement();
        mockMovement.setAccountId(movementId);
        mockMovement.setMovementValue(BigDecimal.valueOf(500));

        mockMovementDTO = new MovementDTO();
        mockMovementDTO.setAccountId(movementId);
        mockMovementDTO.setMovementValue(BigDecimal.valueOf(500));
    }

    @Test
    void givenSaveMove_WhenMoveIsValid_ThenSaveSuccessfully() {
        Account account = new Account();
        account.setAccountId(1L);
        account.setInitialBalance(BigDecimal.valueOf(1000));

        when(accountService.findById(mockMovementDTO.getAccountId())).thenReturn(account);
        when(accountService.saveAccount(any(AccountDTO.class), eq(user))).thenReturn(new AccountDTO());
        when(repository.save(any(Movement.class))).thenReturn(new Movement());

        service.saveMovement(mockMovementDTO, user);

        assertEquals(account.getInitialBalance(), BigDecimal.valueOf(1500));
        verify(accountService, times(1)).findById(mockMovementDTO.getAccountId());
        verify(accountService, times(1)).saveAccount(any(AccountDTO.class), eq(user));
        verify(repository, times(1)).save(any(Movement.class));
    }

    @Test
    void givenSaveMove_WhenMoveIsNegative_ThenSaveSuccessfully() {
        mockMovementDTO.setMovementValue(BigDecimal.valueOf(-500)); // Negative movement

        Account account = new Account();
        account.setAccountId(1L);
        account.setInitialBalance(BigDecimal.valueOf(1000));

        when(accountService.findById(mockMovementDTO.getAccountId())).thenReturn(account);
        when(accountService.saveAccount(any(AccountDTO.class), eq(user))).thenReturn(new AccountDTO());
        when(repository.save(any(Movement.class))).thenReturn(new Movement());

        service.saveMovement(mockMovementDTO, user);

        verify(accountService, times(1)).findById(mockMovementDTO.getAccountId());
        verify(accountService, times(1)).saveAccount(any(AccountDTO.class), eq(user));
        verify(repository, times(1)).save(any(Movement.class));
    }

    @Test
    void givenSaveMovement_WhenTheAmountExceeds_ThenThrowExceptionForInvalidMovement() {
        mockMovementDTO.setMovementValue(BigDecimal.valueOf(-2000)); // Exceeds balance

        Account accountEntity = new Account();
        accountEntity.setAccountId(1L);
        accountEntity.setInitialBalance(BigDecimal.valueOf(1000));

        when(accountService.findById(mockMovementDTO.getAccountId())).thenReturn(accountEntity);

        assertThrows(MovementException.class, () -> service.saveMovement(mockMovementDTO, user));

        verify(accountService, times(1)).findById(mockMovementDTO.getAccountId());
        verify(accountService, times(0)).saveAccount(any(AccountDTO.class), eq(user));
        verify(repository, times(0)).save(any(Movement.class));
    }

    @Test
    void givenUpdateEntity_WhenEntityExists_ThenReturnUpdatedEntity(){
        when(repository.findById(movementId)).thenReturn(Optional.of(mockMovement));
        when(repository.save(any(Movement.class))).thenReturn(mockMovement);
        MovementDTO result = service.updateMovement(movementId, mockMovementDTO, user);
        assertNotNull(result);
        verify(repository, times(1)).findById(movementId);
        verify(repository, times(1)).save(any());
    }

    @Test
    void givenDeleteLogic_WhenMovementExists_ThenMarkMovementAsInactive() {
        when(repository.findById(movementId)).thenReturn(Optional.of(mockMovement));
        when(repository.save(mockMovement)).thenReturn(mockMovement);

        service.deleteLogic(movementId, user);

        verify(repository, times(1)).findById(movementId);
        verify(repository, times(1)).save(mockMovement);
        assertEquals(Boolean.FALSE, mockMovement.getStatus());
        assertEquals(user, mockMovement.getLastModifiedByUser());
    }

    @Test
    void givenDeleteLogic_WhenMovementDoesNotExist_ThenThrowModelNotFoundException() {
        when(repository.findById(movementId)).thenReturn(Optional.empty());
        assertThrows(ModelNotFoundException.class, () -> service.deleteLogic(movementId, user));
        verify(repository, times(1)).findById(movementId);
        verify(repository, times(0)).save(any(Movement.class));
    }

}