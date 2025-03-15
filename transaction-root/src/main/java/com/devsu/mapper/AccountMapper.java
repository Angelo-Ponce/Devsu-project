package com.devsu.mapper;

import com.devsu.dto.AccountDTO;
import com.devsu.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    AccountDTO toAccountDTO(Account account);

    Account toAccount(AccountDTO accountDTO);
}
