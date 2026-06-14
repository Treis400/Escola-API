package com.example.escola;

import com.example.escola.dto.AlunoRequestDTO;
import com.example.escola.dto.AlunoResponseDTO;
import com.example.escola.dto.AlunoUpdateDTO;
import com.example.escola.dto.MateriaDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestMethodOrder(MethodOrderer.DisplayName.class)
class EscolaApiIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    @DisplayName("C1-T1 Inserir aluno válido com matrícula automática retorna 201")
    void c1_t1_inserirAlunoValido() {
        AlunoRequestDTO dto = new AlunoRequestDTO();
        dto.setNome("Thiago Reis");
        dto.setSexo("M");
        dto.setNascimento("01/01/2000");

        MateriaDTO materia = new MateriaDTO();
        materia.setMateria("Matematica");
        materia.setNotaFinal("9.5");
        dto.setMaterias(List.of(materia));

        ResponseEntity<AlunoResponseDTO> response = restTemplate.postForEntity(
                url("/api/alunos"), dto, AlunoResponseDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getNome()).isEqualTo("Thiago Reis");
        assertThat(response.getBody().getMatricula()).isEqualTo(7L);
        assertThat(response.getBody().getMaterias()).hasSize(1);
        assertThat(response.getBody().getMaterias().get(0).getNotaFinal()).isEqualTo("9.5");
    }

    @Test
    @DisplayName("C1-T2 Inserir aluno com matrícula duplicada retorna 400")
    void c1_t2_inserirAlunoMatriculaDuplicada() {
        AlunoRequestDTO dto = new AlunoRequestDTO();
        dto.setMatricula("1");
        dto.setNome("Duplicado Silva");
        dto.setSexo("M");
        dto.setNascimento("01/01/2000");

        ResponseEntity<Map> response = restTemplate.postForEntity(
                url("/api/alunos"), dto, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().get("message").toString()).contains("1");
    }

    @Test
    @DisplayName("C1-T3 Inserir aluno com nome contendo números retorna 400 com erro de validação")
    void c1_t3_inserirAlunoNomeInvalido() {
        AlunoRequestDTO dto = new AlunoRequestDTO();
        dto.setNome("Thiago123");
        dto.setSexo("M");
        dto.setNascimento("01/01/2000");

        ResponseEntity<Map> response = restTemplate.postForEntity(
                url("/api/alunos"), dto, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().get("error")).isEqualTo("Validation Error");
        assertThat(response.getBody()).containsKey("messages");
    }

    @Test
    @DisplayName("C2-T1 Atualizar aluno existente adicionando nova matéria retorna 200")
    void c2_t1_atualizarAlunoExistente() {
        AlunoUpdateDTO dto = new AlunoUpdateDTO();
        dto.setNome("Erik Oliver Atualizado");
        dto.setSexo("M");
        dto.setNascimento("10/10/1987");

        MateriaDTO novaMateria = new MateriaDTO();
        novaMateria.setMateria("Historia");
        novaMateria.setNotaFinal("7.0");
        dto.setMateriasParaAdicionar(List.of(novaMateria));
        dto.setMateriasParaRemoverIds(List.of());

        HttpEntity<AlunoUpdateDTO> entity = new HttpEntity<>(dto);
        ResponseEntity<AlunoResponseDTO> response = restTemplate.exchange(
                url("/api/alunos/1"), HttpMethod.PUT, entity, AlunoResponseDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getNome()).isEqualTo("Erik Oliver Atualizado");
        assertThat(response.getBody().getMaterias()).hasSize(3);
    }

    @Test
    @DisplayName("C2-T2 Atualizar aluno inexistente retorna 404 com mensagem de erro")
    void c2_t2_atualizarAlunoInexistente() {
        AlunoUpdateDTO dto = new AlunoUpdateDTO();
        dto.setNome("Fantasma Silva");
        dto.setSexo("M");
        dto.setNascimento("01/01/2000");
        dto.setMateriasParaAdicionar(List.of());
        dto.setMateriasParaRemoverIds(List.of());

        HttpEntity<AlunoUpdateDTO> entity = new HttpEntity<>(dto);
        ResponseEntity<Map> response = restTemplate.exchange(
                url("/api/alunos/9999"), HttpMethod.PUT, entity, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().get("error")).isEqualTo("Not Found");
        assertThat(response.getBody().get("message").toString()).contains("9999");
    }

    @Test
    @DisplayName("C2-T3 Atualizar aluno com sexo inválido retorna 400 com erro de validação")
    void c2_t3_atualizarAlunoSexoInvalido() {
        AlunoUpdateDTO dto = new AlunoUpdateDTO();
        dto.setNome("Erik Oliver");
        dto.setSexo("X");
        dto.setNascimento("10/10/1987");
        dto.setMateriasParaAdicionar(List.of());
        dto.setMateriasParaRemoverIds(List.of());

        HttpEntity<AlunoUpdateDTO> entity = new HttpEntity<>(dto);
        ResponseEntity<Map> response = restTemplate.exchange(
                url("/api/alunos/1"), HttpMethod.PUT, entity, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().get("error")).isEqualTo("Validation Error");
    }

    @Test
    @DisplayName("C3-T1 Filtro padrão (>8) retorna apenas matérias com nota maior que 8")
    void c3_t1_listarNotasSuperioresDefault() {
        ResponseEntity<AlunoResponseDTO[]> response = restTemplate.getForEntity(
                url("/api/alunos/notas-superiores"), AlunoResponseDTO[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull().isNotEmpty();

        for (AlunoResponseDTO aluno : response.getBody()) {
            for (MateriaDTO materia : aluno.getMaterias()) {
                double nota = Double.parseDouble(materia.getNotaFinal());
                assertThat(nota)
                        .as("Matéria '%s' do aluno %d deve ter nota > 8",
                                materia.getMateria(), aluno.getMatricula())
                        .isGreaterThan(8.0);
            }
        }
    }

    @Test
    @DisplayName("C3-T2 Filtro customizado nota>5 retorna quantidade igual ou maior que nota>8")
    void c3_t2_listarNotasSuperioresCustomizado() {
        ResponseEntity<AlunoResponseDTO[]> r8 = restTemplate.getForEntity(
                url("/api/alunos/notas-superiores"), AlunoResponseDTO[].class);
        ResponseEntity<AlunoResponseDTO[]> r5 = restTemplate.getForEntity(
                url("/api/alunos/notas-superiores?nota=5"), AlunoResponseDTO[].class);

        assertThat(r5.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(r5.getBody()).isNotNull();
        assertThat(r5.getBody().length).isGreaterThanOrEqualTo(r8.getBody().length);
    }

    @Test
    @DisplayName("C3-T3 Alunos sem notas acima do filtro não aparecem no resultado")
    void c3_t3_alunosSemNotasSuperioresAusentes() {
        ResponseEntity<AlunoResponseDTO[]> response = restTemplate.getForEntity(
                url("/api/alunos/notas-superiores"), AlunoResponseDTO[].class);

        assertThat(response.getBody()).isNotNull();

        List<Long> matriculasRetornadas = Arrays.stream(response.getBody())
                .map(AlunoResponseDTO::getMatricula)
                .toList();

        assertThat(matriculasRetornadas)
                .as("Alunos 3, 4 e 5 não devem aparecer no filtro de notas > 8")
                .doesNotContain(3L, 4L, 5L);
    }
}
