package com.devsu.repository;

import com.devsu.model.Account;

import java.util.List;

public interface IAccountRepository extends IGenericRepository<Account, Long> {

    List<Account> findByPersonId(Long personId);
}
