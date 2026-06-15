package com.example.escola.service;

import com.example.escola.dto.AlunoRequestDTO;
import com.example.escola.dto.AlunoResponseDTO;
import com.example.escola.dto.AlunoUpdateDTO;
import com.example.escola.dto.MateriaDTO;
import com.example.escola.exception.ResourceNotFoundException;
import com.example.escola.model.Aluno;
import com.example.escola.model.AlunoMateria;
import com.example.escola.repository.AlunoMateriaRepository;
import com.example.escola.repository.AlunoRepository;
import com.example.escola.util.ConversaoUtil;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final AlunoMateriaRepository alunoMateriaRepository;

    public AlunoService(AlunoRepository alunoRepository, AlunoMateriaRepository alunoMateriaRepository) {
        this.alunoRepository = alunoRepository;
        this.alunoMateriaRepository = alunoMateriaRepository;
    }

    @Transactional
    public AlunoResponseDTO inserirAluno(AlunoRequestDTO dto) {
        Long matricula = Long.parseLong(dto.getMatricula());

        if (alunoRepository.existsById(matricula)) {
            throw new IllegalArgumentException("Ja existe um aluno cadastrado com a matricula " + matricula);
        }

        alunoRepository.findByNomeIgnoreCase(dto.getNome()).ifPresent(existente -> {
            throw new IllegalArgumentException(
                "Ja existe um aluno com o nome '" + existente.getNome() + "' (matricula " + existente.getMatricula() + ")." +
                " O sistema nao diferencia maiusculas de minusculas."
            );
        });

        LocalDate nascimento = ConversaoUtil.paraLocalDate(dto.getNascimento());
        if (nascimento.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A data de nascimento nao pode ser uma data futura: " + dto.getNascimento());
        }

        Aluno aluno = new Aluno(matricula, dto.getNome(), dto.getSexo(), nascimento);

        if (dto.getMaterias() != null) {
            for (MateriaDTO m : dto.getMaterias()) {
                AlunoMateria materia = new AlunoMateria();
                materia.setMateria(m.getMateria());
                materia.setNotaFinal(ConversaoUtil.paraBigDecimal(m.getNotaFinal()));
                aluno.addMateria(materia);
            }
        }

        return toResponseDTO(alunoRepository.save(aluno));
    }

    @Transactional
    public AlunoResponseDTO atualizarAluno(@NonNull Long matricula, AlunoUpdateDTO dto) {
        Aluno aluno = alunoRepository.findById(matricula)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno com matricula " + matricula + " nao encontrado"));

        LocalDate nascimento = ConversaoUtil.paraLocalDate(dto.getNascimento());
        if (nascimento.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A data de nascimento nao pode ser uma data futura: " + dto.getNascimento());
        }

        aluno.setNome(dto.getNome());
        aluno.setSexo(dto.getSexo());
        aluno.setNascimento(nascimento);

        aluno.getMaterias().clear();
        alunoRepository.saveAndFlush(aluno);

        for (MateriaDTO m : dto.getMaterias()) {
            AlunoMateria materia = new AlunoMateria();
            materia.setMateria(m.getMateria());
            materia.setNotaFinal(ConversaoUtil.paraBigDecimal(m.getNotaFinal()));
            aluno.addMateria(materia);
        }

        return toResponseDTO(alunoRepository.save(aluno));
    }

    @Transactional(readOnly = true)
    public List<AlunoResponseDTO> listarTodos() {
        return alunoRepository.findAll().stream()
                .sorted(Comparator.comparing(Aluno::getMatricula))
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AlunoResponseDTO> listarComNotasSuperioresA(BigDecimal notaMinima) {
        List<AlunoMateria> aprovadas = alunoMateriaRepository.findByNotaFinalGreaterThan(notaMinima);

        Map<Aluno, List<AlunoMateria>> agrupado = aprovadas.stream()
                .collect(Collectors.groupingBy(AlunoMateria::getAluno));

        return agrupado.entrySet().stream()
                .sorted(Comparator.comparing(e -> e.getKey().getMatricula()))
                .map(entry -> {
                    Aluno aluno = entry.getKey();
                    List<MateriaDTO> materias = entry.getValue().stream()
                            .sorted(Comparator.comparing(AlunoMateria::getId))
                            .map(m -> new MateriaDTO(m.getId(), m.getMateria(), ConversaoUtil.paraTextoNota(m.getNotaFinal())))
                            .collect(Collectors.toList());
                    return new AlunoResponseDTO(aluno.getMatricula(), aluno.getNome(), aluno.getSexo(),
                            ConversaoUtil.paraTextoData(aluno.getNascimento()), materias);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void excluirAluno(@NonNull Long matricula) {
        if (!alunoRepository.existsById(matricula)) {
            throw new ResourceNotFoundException("Aluno com matricula " + matricula + " nao encontrado");
        }
        alunoRepository.deleteById(matricula);
    }

    private AlunoResponseDTO toResponseDTO(Aluno aluno) {
        List<MateriaDTO> materias = aluno.getMaterias().stream()
                .sorted(Comparator.comparing(AlunoMateria::getId))
                .map(m -> new MateriaDTO(m.getId(), m.getMateria(), ConversaoUtil.paraTextoNota(m.getNotaFinal())))
                .collect(Collectors.toList());

        return new AlunoResponseDTO(aluno.getMatricula(), aluno.getNome(), aluno.getSexo(),
                ConversaoUtil.paraTextoData(aluno.getNascimento()), materias);
    }
}
