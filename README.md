# CommuniTracker API

O CommuniTracker é uma API desenvolvida para sustentar o ecossistema financeiro de um Banco Comunitário. Seu objetivo central é promover o desenvolvimento de economias locais, garantindo a rastreabilidade e integridade das finanças através de um motor de partidas dobradas.

Este repositório contém a entrega da infraestrutura e proposta arquitetural para a disciplina de Princípios e Padrões de Arquitetura.

## 🏗️ Arquitetura e Tecnologias

Nesta etapa, a aplicação foi estruturada seguindo os princípios de **Clean Architecture** e **SOLID**, isolando as regras de negócio do framework web e aplicando segregação de responsabilidades.

- **Linguagem:** Java 21
- **Framework:** Spring Boot 4.x
- **Padrões Aplicados:**
  - **Clean Architecture:** Divisão em pacotes (`core`, `domain`, `interfaces`, `usecase`, `repository`, `controller`).
  - **Segregação de Interfaces (CQRS-lite / ISP):** Separação entre contratos de leitura (`AccountQueries`) e contratos de escrita (`AccountRepository`).
  - **Result Pattern:** Com Sealed Interfaces para previsibilidade no retorno dos casos de uso, eliminando exceções como fluxo de controle.
  - **Records:** Entidades e DTOs modelados como Records para garantir imutabilidade.
  - **Single Responsibility em Controllers:** Separação entre gerenciamento da coleção (`AccountsController`) e operações pontuais por recurso (`AccountDetailController`).
- **Banco de Dados:** Atualmente utilizando um DataStore compartilhado em memória (`AccountDataStore`) com implementações mock (`AccountQueriesMock` e `AccountRepositoryMock`). A persistência real com PostgreSQL será implementada nas próximas etapas.

## 🚀 Como Executar o Projeto

O projeto utiliza o **Gradle Wrapper (`gradlew`)**, portanto, você não precisa ter o Gradle instalado na sua máquina, apenas o Java 21.

1. Clone o repositório e acesse a pasta do projeto:

   ```bash
   git clone https://github.com/MintoJazz/communi-tracker.api.git
   cd communi-tracker.api
   ```

2. Mude para a branch da entrega:

   ```bash
   git checkout feature/aula-04-proposta-arquitetura
   ```

3. Execute a aplicação via Gradle Wrapper:
   - **No Linux/Mac:**

     ```bash
     ./gradlew bootRun
     ```

   - **No Windows:**

     ```bash
     gradlew.bat bootRun
     ```

A aplicação iniciará o servidor Tomcat embutido na porta `8080`.

## 🧪 Testes Automatizados

O projeto conta com uma suíte abrangente de testes unitários e de integração utilizando **JUnit 5**, **Mockito** e **Spring Boot Test** (`@WebMvcTest`, `@MockitoBean`, `@Mock`).

### Executando os Testes

Para garantir a qualidade da aplicação, execute:

- **No Linux/Mac:**

  ```bash
  ./gradlew test
  ```

- **No Windows:**

  ```bash
  gradlew.bat test
  ```

Os testes cobrem:
- **Use Cases:** Regras de negócio, fluxos de sucesso e validações de erro (`ListAccountsUseCaseTest`, `GetAccountUseCaseTest`, `CreateAccountUseCaseTest`).
- **Controllers:** Rotas REST, status HTTP e payloads (`HealthControllerTest`, `AccountsControllerTest`, `AccountDetailControllerTest`).
- **Repositório em Memória:** Integridade de consultas e geração de IDs (`AccountQueriesMockTest`, `AccountRepositoryMockTest`).

Ao final da execução, todos os testes devem passar com `BUILD SUCCESSFUL`. Os relatórios ficam salvos no diretório `build/reports/tests/test/index.html`.

## 📍 Endpoints Disponíveis

Com a aplicação rodando, você pode testar as seguintes rotas via Postman, Insomnia ou cURL:

### 1. Health Check

Verifica se a API está no ar.

```bash
curl http://localhost:8080/health
```

**Retorno esperado (200 OK):**

```json
{
  "status": "ok"
}
```

### 2. Listagem de Contas

Retorna a lista de todas as contas solidárias cadastradas.

```bash
curl http://localhost:8080/accounts
```

**Retorno esperado (200 OK):**

```json
[
  {
    "id": 1,
    "owner": "Fundo Solidário Principal",
    "balance": 15000.00
  },
  {
    "id": 2,
    "owner": "Empreendimento Local",
    "balance": 350.50
  }
]
```

### 3. Busca de Conta por ID

Retorna os detalhes de uma conta específica pelo seu identificador.

```bash
curl http://localhost:8080/accounts/1
```

**Retorno esperado (200 OK):**

```json
{
  "id": 1,
  "owner": "Fundo Solidário Principal",
  "balance": 15000.00
}
```

**Caso de Conta Inexistente (404 Not Found):**

```bash
curl http://localhost:8080/accounts/999
```

```json
{
  "error": "Conta não encontrada."
}
```

### 4. Criação de Conta Solidária

Cadastra uma nova conta comunitária no sistema.

```bash
curl -X POST http://localhost:8080/accounts \
  -H "Content-Type: application/json" \
  -d '{
    "owner": "Coletivo de Artesãs",
    "initialBalance": 1200.00
  }'
```

**Retorno esperado (201 Created):**

```json
{
  "id": 3,
  "owner": "Coletivo de Artesãs",
  "balance": 1200.00
}
```

**Validações e Casos de Erro (400 Bad Request):**
- Caso o campo `owner` seja nulo ou vazio:
  ```json
  {
    "error": "O nome do titular é obrigatório."
  }
  ```
- Caso o campo `initialBalance` seja nulo ou negativo:
  ```json
  {
    "error": "O saldo inicial não pode ser negativo."
  }
  ```
