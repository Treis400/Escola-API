# Escola API

API REST desenvolvida com **Spring Boot 3** (Java 17) + **JPA/Hibernate** + banco H2 em memória,
implementando o cadastro de alunos e suas matérias/notas.

## Modelo de dados

- **REGISTRO_ALUNO**
  - `matricula` (PK, 1–999)
  - `nome`
  - `sexo` ('M' ou 'F')
  - `nascimento` (data)

- **ALUNO_MATERIAS**
  - `id` (PK, gerado automaticamente)
  - `matricula` (FK → REGISTRO_ALUNO.matricula)
  - `materia`
  - `nota_final` (decimal 0.0–10.0)

Os dados iniciais (6 alunos e 8 registros de matérias) são carregados automaticamente via `data.sql`.

## Como executar

```bash
mvn spring-boot:run
```

A aplicação inicia em `http://localhost:8080`.
Console H2 disponível em `http://localhost:8080/h2-console`.

## Endpoints

| Método | URL | Descrição |
|--------|-----|-----------|
| POST | `/api/alunos` | Cadastrar novo aluno com matérias |
| PUT | `/api/alunos/{matricula}` | Atualizar dados e matérias do aluno |
| DELETE | `/api/alunos/{matricula}` | Excluir aluno e suas matérias |
| GET | `/api/alunos` | Listar todos os alunos |
| GET | `/api/alunos/notas-superiores?nota=8` | Listar alunos com nota acima do valor informado |

## Validações

| Campo | Regra |
|-------|-------|
| `matricula` | Obrigatória; somente números; entre 1 e 999; única no banco |
| `nome` | Obrigatório; apenas letras e espaços; único no banco (sem distinção de maiúsculas) |
| `sexo` | Obrigatório; apenas `M` ou `F` |
| `nascimento` | Obrigatório; formato `dd/MM/yyyy`; data real; não pode ser futura |
| `materia` | Obrigatório; apenas letras e espaços |
| `notaFinal` | Obrigatório; decimal entre `0` e `10.0`; separador: ponto (`.`) |

## Documentação completa

Ver pasta [`anotacoes/`](anotacoes/) para documentação detalhada de endpoints,
validações, exceções e testes.
