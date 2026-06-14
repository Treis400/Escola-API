package com.example.escola.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.ArrayList;
import java.util.List;

public class AlunoUpdateDTO {

    @NotBlank(message = "O nome e obrigatorio")
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ ]+$", message = "O nome deve conter apenas letras, sem numeros ou caracteres especiais")
    private String nome;

    @NotBlank(message = "O sexo e obrigatorio")
    @Pattern(regexp = "^[MF]$", message = "O sexo deve ser 'M' ou 'F'")
    private String sexo;

    @NotBlank(message = "A data de nascimento e obrigatoria")
    @Pattern(regexp = "^\\d{2}/\\d{2}/\\d{4}$", message = "A data de nascimento deve seguir o formato dd/MM/yyyy")
    private String nascimento;

    @Valid
    private List<MateriaDTO> materias = new ArrayList<>();

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public String getNascimento() { return nascimento; }
    public void setNascimento(String nascimento) { this.nascimento = nascimento; }

    public List<MateriaDTO> getMaterias() { return materias; }
    public void setMaterias(List<MateriaDTO> materias) { this.materias = materias; }
}
