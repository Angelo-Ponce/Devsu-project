package com.devsu.controller;

import com.devsu.dto.MovementDTO;
import com.devsu.dto.MovementReportDTO;
import com.devsu.mapper.MovementMapper;
import com.devsu.model.Movement;
import com.devsu.service.IMovementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.devsu.constants.Constants.USER;

@RestController
@RequestMapping("api/v1/movimientos")
@RequiredArgsConstructor
public class MovementController {

    private final IMovementService service;

    @GetMapping
    public ResponseEntity<List<MovementDTO>> findAll(){
        List<MovementDTO> list = service.findAll().stream()
                .map(MovementMapper.INSTANCE::toMovementDTO).toList();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovementDTO> getMovementById(@PathVariable("id") Long id) {
        Movement entity = service.findById(id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(MovementMapper.INSTANCE.toMovementDTO(entity));
    }

    @GetMapping("/reportes")
    public ResponseEntity<List<MovementReportDTO>> reportMovementByDateAndClientId(@RequestParam(value = "clientId") String clientId,
                                                                                   @RequestParam(value = "startDate") LocalDateTime startDate,
                                                                                   @RequestParam(value = "endDate") LocalDateTime endDate) {
        List<MovementReportDTO> reportDTO = service.reportMovementByDateAndClientId(clientId, startDate, endDate);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(reportDTO);
    }

    @PostMapping
    public ResponseEntity<MovementDTO> save(@Valid @RequestBody MovementDTO request) throws Exception {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.saveMovement(request, USER));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovementDTO> update(@PathVariable("id") Long id, @Valid @RequestBody MovementDTO dto) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.updateMovement(id, dto, USER));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete( @PathVariable("id") Long id){
        service.deleteLogic(id, USER);
        return ResponseEntity.noContent().build();
    }

}
