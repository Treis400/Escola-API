package com.example.escola.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "aluno_materias")
public class AlunoMateria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matricula", nullable = false)
    @JsonIgnoreProperties("materias")
    private Aluno aluno;

    @Column(name = "materia", nullable = false)
    private String materia;

    @Column(name = "nota_final", nullable = false, precision = 4, scale = 2)
    private BigDecimal notaFinal;

    public AlunoMateria() {
    }

    public AlunoMateria(Aluno aluno, String materia, BigDecimal notaFinal) {
        this.aluno = aluno;
        this.materia = materia;
        this.notaFinal = notaFinal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public BigDecimal getNotaFinal() {
        return notaFinal;
    }

    public void setNotaFinal(BigDecimal notaFinal) {
        this.notaFinal = notaFinal;
    }
}
