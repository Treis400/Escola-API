package com.example.escola.repository;

import com.example.escola.model.AlunoMateria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface AlunoMateriaRepository extends JpaRepository<AlunoMateria, Long> {

    List<AlunoMateria> findByNotaFinalGreaterThan(BigDecimal nota);
}
