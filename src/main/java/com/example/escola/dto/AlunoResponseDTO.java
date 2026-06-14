package com.example.escola.dto;

import java.util.ArrayList;
import java.util.List;

public class AlunoResponseDTO {

    private Long matricula;
    private String nome;
    private String sexo;
    private String nascimento;
    private List<MateriaDTO> materias = new ArrayList<>();

    public AlunoResponseDTO() {}

    public AlunoResponseDTO(Long matricula, String nome, String sexo, String nascimento, List<MateriaDTO> materias) {
        this.matricula = matricula;
        this.nome = nome;
        this.sexo = sexo;
        this.nascimento = nascimento;
        this.materias = materias;
    }

    public Long getMatricula() { return matricula; }
    public void setMatricula(Long matricula) { this.matricula = matricula; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public String getNascimento() { return nascimento; }
    public void setNascimento(String nascimento) { this.nascimento = nascimento; }

    public List<MateriaDTO> getMaterias() { return materias; }
    public void setMaterias(List<MateriaDTO> materias) { this.materias = materias; }
}
