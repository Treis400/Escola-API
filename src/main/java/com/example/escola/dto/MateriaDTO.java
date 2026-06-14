package com.example.escola.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class MateriaDTO {

    private Long id;

    @NotBlank(message = "O nome da materia e obrigatorio")
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ ]+$", message = "A materia deve conter apenas letras, sem numeros ou caracteres especiais")
    private String materia;

    @NotBlank(message = "A nota final e obrigatoria")
    @Pattern(
        regexp = "^(10(\\.0+)?|[0-9](\\.\\d+)?)$",
        message = "A nota deve ser um numero decimal entre 0.0 e 10.0. Exemplo: 8.5"
    )
    private String notaFinal;

    public MateriaDTO() {}

    public MateriaDTO(Long id, String materia, String notaFinal) {
        this.id = id;
        this.materia = materia;
        this.notaFinal = notaFinal;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMateria() { return materia; }
    public void setMateria(String materia) { this.materia = materia; }

    public String getNotaFinal() { return notaFinal; }
    public void setNotaFinal(String notaFinal) { this.notaFinal = notaFinal; }
}
