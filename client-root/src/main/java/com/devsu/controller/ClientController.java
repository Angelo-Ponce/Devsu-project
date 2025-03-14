package com.devsu.controller;

import com.devsu.dto.ClientDTO;
import com.devsu.model.Client;
import com.devsu.service.IClientService;
import com.devsu.util.MapperUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/v1/clientes")
@RequiredArgsConstructor
public class ClientController {

    private final IClientService service;
    private final MapperUtil mapperUtil;

    private static final String USER = "Angelo";

    @GetMapping
    public ResponseEntity<List<ClientDTO>> findAll() {
        List<ClientDTO> clientDTOList = mapperUtil.mapList(service.findAll(), ClientDTO.class);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(clientDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> findById(@PathVariable("id") Long id) {
        Client obj = service.findById(id);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(mapperUtil.map(obj, ClientDTO.class));
    }

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody ClientDTO clientDTO) {
        ClientDTO client = service.save(clientDTO, USER);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(client.getPersonId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> update( @PathVariable("id") Long id, @RequestBody ClientDTO dto) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.update(id, dto, USER));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete( @PathVariable("id") Long id){
        service.deleteLogic(id, USER);
        return ResponseEntity.noContent().build();
    }
}
