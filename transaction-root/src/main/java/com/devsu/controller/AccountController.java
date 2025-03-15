package com.devsu.controller;

import com.devsu.dto.AccountDTO;
import com.devsu.mapper.AccountMapper;
import com.devsu.model.Account;
import com.devsu.service.IAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static com.devsu.constants.Constants.USER;

@RestController
@RequestMapping("api/v1/cuentas")
@RequiredArgsConstructor
public class AccountController {

    private final IAccountService service;

    @GetMapping
    public ResponseEntity<List<AccountDTO>> findAll() {
        List<AccountDTO> list = service.findAll().stream()
                .map(AccountMapper.INSTANCE::toAccountDTO).toList();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable("id") Long id) {
        Account entity = service.findById(id);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(AccountMapper.INSTANCE.toAccountDTO(entity));
    }

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody AccountDTO request) {
        AccountDTO accountDTO = service.saveAccount(request, USER);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(accountDTO.getAccountId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountDTO> update(@PathVariable("id") Long id, @Valid @RequestBody AccountDTO dto) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.updateAccount(id, dto, USER));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete( @PathVariable("id") Long id){
        service.deleteLogic(id, USER);
        return ResponseEntity.noContent().build();
    }
}
