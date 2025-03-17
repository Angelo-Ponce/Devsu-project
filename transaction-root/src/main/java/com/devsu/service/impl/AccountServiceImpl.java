package com.devsu.service.impl;

import com.devsu.dto.AccountDTO;
import com.devsu.exception.ModelNotFoundException;
import com.devsu.mapper.AccountMapper;
import com.devsu.model.Account;
import com.devsu.repository.IAccountRepository;
import com.devsu.repository.IGenericRepository;
import com.devsu.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl extends GenericServiceImpl<Account, Long> implements IAccountService {

    private final IAccountRepository repository;

    @Override
    protected IGenericRepository<Account, Long> getRepository() {
        return repository;
    }

    @Override
    public AccountDTO saveAccount(AccountDTO accountDTO, String user) {
        Account account = AccountMapper.INSTANCE.toAccount(accountDTO);
        account.setCreatedByUser(user);
        return AccountMapper.INSTANCE.toAccountDTO(repository.save(account));
    }

    @Override
    public AccountDTO updateAccount(Long id, AccountDTO accountDTO, String user) {
        Account accountEntity = repository.findById(id)
                .map( account -> {
                    account.setAccountNumber(accountDTO.getAccountNumber());
                    account.setAccountType(accountDTO.getAccountType());
                    account.setInitialBalance(accountDTO.getInitialBalance());
                    account.setStatus(accountDTO.getStatus());
                    account.setPersonId(accountDTO.getPersonId());
                    account.setLastModifiedByUser(user);
                    return repository.save(account);
                }).orElseThrow(() -> new ModelNotFoundException("ID not found: " + id));
        return AccountMapper.INSTANCE.toAccountDTO(accountEntity);
    }

    @Override
    public void deleteLogic(Long id, String user) {
        Account account = this.findById(id);
        account.setStatus(Boolean.FALSE);
        account.setLastModifiedByUser(user);
        repository.save(account);
    }
}
