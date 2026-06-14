package com.example.escola.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "registro_aluno")
public class Aluno {

    @Id
    @Column(name = "matricula")
    private Long matricula;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "sexo", nullable = false, length = 1)
    private String sexo;

    @Column(name = "nascimento", nullable = false)
    private LocalDate nascimento;

    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnoreProperties("aluno")
    private List<AlunoMateria> materias = new ArrayList<>();

    public Aluno() {
    }

    public Aluno(Long matricula, String nome, String sexo, LocalDate nascimento) {
        this.matricula = matricula;
        this.nome = nome;
        this.sexo = sexo;
        this.nascimento = nascimento;
    }

    public Long getMatricula() {
        return matricula;
    }

    public void setMatricula(Long matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public LocalDate getNascimento() {
        return nascimento;
    }

    public void setNascimento(LocalDate nascimento) {
        this.nascimento = nascimento;
    }

    public List<AlunoMateria> getMaterias() {
        return materias;
    }

    public void setMaterias(List<AlunoMateria> materias) {
        this.materias = materias;
    }

    public void addMateria(AlunoMateria materia) {
        materias.add(materia);
        materia.setAluno(this);
    }

    public void removeMateria(AlunoMateria materia) {
        materias.remove(materia);
        materia.setAluno(null);
    }
}
