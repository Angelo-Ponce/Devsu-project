package com.devsu.service.impl;

import com.devsu.dto.ClientDTO;
import com.devsu.dto.MovementDTO;
import com.devsu.dto.MovementReportDTO;
import com.devsu.exception.MovementException;
import com.devsu.mapper.AccountMapper;
import com.devsu.mapper.MovementMapper;
import com.devsu.model.Account;
import com.devsu.model.Movement;
import com.devsu.repository.IGenericRepository;
import com.devsu.repository.IMovementRepository;
import com.devsu.service.IAccountService;
import com.devsu.service.IMovementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.devsu.constants.Constants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovementServiceImpl extends GenericServiceImpl<Movement, Long> implements IMovementService {

    private final IMovementRepository repository;

    private final IAccountService accountService;

    private final ClientService clientService;

    @Override
    protected IGenericRepository<Movement, Long> getRepository() {
        return repository;
    }

    @Override
    public MovementDTO saveMovement(MovementDTO movementDTO, String user) {
        Account account = this.accountService.findById(movementDTO.getAccountId());
        BigDecimal balance = account.getInitialBalance().add(movementDTO.getMovementValue());
        Movement movement = MovementMapper.INSTANCE.toMovement(movementDTO);
        validMovement(movement, balance);

        // Actualizar detalles del movimiento y la cuenta
        updateMovementDetails(movement, account, balance, user);

        this.accountService.saveAccount(AccountMapper.INSTANCE.toAccountDTO(account), user);

        return MovementMapper.INSTANCE.toMovementDTO(repository.save(movement));
    }

    @Override
    public MovementDTO updateMovement(Long id, MovementDTO movementDTO, String user) {
        Movement movementEntity = repository.findById(id)
                .map(movement -> {
                    movement.setAccountId(movementDTO.getAccountId());
                    movement.setMovementDate(movementDTO.getMovementDate());
                    movement.setMovementType(movementDTO.getMovementType());
                    movement.setMovementValue(movementDTO.getMovementValue());
                    movement.setBalance(movementDTO.getBalance());
                    movement.setStatus(movementDTO.getStatus());
                    movement.setLastModifiedByUser(user);
                    return repository.save(movement);
                }).orElseThrow(() -> new MovementException("ID not found: " + id));
        return MovementMapper.INSTANCE.toMovementDTO(movementEntity);
    }

    @Override
    public void deleteLogic(Long id, String user) {
        Movement movement = this.findById(id);
        movement.setStatus(false);
        movement.setLastModifiedByUser(user);
        repository.save(movement);
    }

    @Override
    public List<MovementReportDTO> reportMovementByDateAndClientId(String clientId, LocalDateTime startDate, LocalDateTime endDate) {
        ClientDTO client = clientService.findClientById(clientId);
        if (client == null) {
            throw new MovementException(CLIENT_NOT_FOUND);
        }
        List<Movement> movementList = repository.findMovementByAccount_PersonIdAndMovementDateBetween(client.getPersonId(), startDate, endDate);
        return movementList.stream()
                .map(movement -> MovementReportDTO.builder()
                        .movementDate(movement.getMovementDate())
                        .name(client.getName())
                        .accountNumber(movement.getAccount().getAccountNumber())
                        .accountType(movement.getAccount().getAccountType())
                        .initialBalance(movement.getBalance())
                        .movementStatus(movement.getStatus())
                        .movementValue(movement.getMovementValue())
                        .balance(movement.getAccount().getInitialBalance())
                        .build())
                .toList();
    }

    private void validMovement(Movement movement, BigDecimal balance){
        if(balance.compareTo(BigDecimal.ZERO) < 0 ) {
            throw new MovementException(BALANCE_NOT_AVAILABLE);
        }
        if (movement.getMovementValue().compareTo(BigDecimal.ZERO) == 0) {
            throw new MovementException(INVALID_MOVEMENT);
        }
    }

    private void updateMovementDetails(Movement movement, Account account, BigDecimal newBalance, String user) {
        movement.setMovementType(determineMovementType(movement.getMovementValue()));
        movement.setAccountId(account.getAccountId());
        movement.setMovementDate(LocalDateTime.now());
        movement.setBalance(account.getInitialBalance());
        movement.setStatus(Boolean.TRUE);
        movement.setCreatedByUser(user);

        account.setInitialBalance(newBalance);
    }

    private String determineMovementType(BigDecimal movementValue) {
        return movementValue.compareTo(BigDecimal.ZERO) > 0
                ? DEPOSIT
                : WITHDRAWAL;
    }
}
