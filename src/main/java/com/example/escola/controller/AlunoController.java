package com.example.escola.controller;

import com.example.escola.dto.AlunoRequestDTO;
import com.example.escola.dto.AlunoResponseDTO;
import com.example.escola.dto.AlunoUpdateDTO;
import com.example.escola.service.AlunoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @PostMapping
    public ResponseEntity<AlunoResponseDTO> inserirAluno(@Valid @RequestBody AlunoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(alunoService.inserirAluno(dto));
    }

    @PutMapping("/{matricula}")
    public ResponseEntity<AlunoResponseDTO> atualizarAluno(@PathVariable @NonNull Long matricula,
                                                            @Valid @RequestBody AlunoUpdateDTO dto) {
        return ResponseEntity.ok(alunoService.atualizarAluno(matricula, dto));
    }

    @GetMapping
    public ResponseEntity<List<AlunoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(alunoService.listarTodos());
    }

    @DeleteMapping("/{matricula}")
    public ResponseEntity<Void> excluirAluno(@PathVariable @NonNull Long matricula) {
        alunoService.excluirAluno(matricula);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/notas-superiores")
    public ResponseEntity<List<AlunoResponseDTO>> listarComNotasSuperiores(
            @RequestParam(defaultValue = "8") BigDecimal nota) {
        return ResponseEntity.ok(alunoService.listarComNotasSuperioresA(nota));
    }
}
