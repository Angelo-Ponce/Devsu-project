package com.devsu.service;

import com.devsu.dto.AccountDTO;
import com.devsu.model.Account;

import java.util.List;

public interface IAccountService extends IGenericService<Account, Long> {

    AccountDTO saveAccount(AccountDTO accountDTO, String user);
    AccountDTO updateAccount(Long id, AccountDTO accountDTO, String user);
    void deleteLogic(Long id, String user);
}
