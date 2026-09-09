# CommuniTracker API

O CommuniTracker é uma API desenvolvida para sustentar o ecossistema financeiro de um Banco Comunitário. Seu objetivo central é promover o desenvolvimento de economias locais, garantindo a rastreabilidade e integridade das finanças através de um motor de partidas dobradas.

Este repositório contém a entrega da infraestrutura inicial (Esboço Arquitetural) para a disciplina de Princípios e Padrões de Arquitetura.

## 🏗️ Arquitetura e Tecnologias

Nesta etapa inicial, a aplicação foi estruturada seguindo os princípios de Clean Architecture, isolando as regras de negócio do framework web.

- **Linguagem:** Java 21
- **Framework:** Spring Boot 3.x / 4.x
- **Padrões Aplicados:**
  - **Clean Architecture:** Divisão em pacotes (`core`, `domain`, `usecase`, `repository`, `controller`).
  - **Result Pattern:** Com Sealed Interfaces para previsibilidade no retorno dos casos de uso.
  - **Records:** Entidades de domínio modeladas como Records para garantir imutabilidade.
- **Banco de Dados:** Atualmente utilizando um Mock Repository em memória (a persistência real com PostgreSQL será implementada nas próximas etapas).

## 🚀 Como Executar o Projeto

O projeto utiliza o **Gradle Wrapper (`gradlew`)** *(nota: no seu esboço estava Maven, mas o projeto está configurado com Gradle)*, portanto, você não precisa ter o Gradle instalado na sua máquina, apenas o Java 21.

1. Clone o repositório e acesse a pasta do projeto:

   ```bash
   git clone https://github.com/MintoJazz/communi-tracker.api.git
   cd communi-tracker.api
   ```

2. Mude para a branch da entrega (se necessário):

   ```bash
   git checkout feature/aula-02-visao-inicial
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

O projeto conta com testes de unidade e integração utilizando **JUnit 5**, **Mockito** e **Spring Boot Test** (`@WebMvcTest`, `@MockitoBean`, `@Mock`).

### Executando os Testes

Para garantir a qualidade da aplicação, você pode rodar as rotinas de teste diretamente pela linha de comando:

- **No Linux/Mac:**

  ```bash
  ./gradlew test
  ```

- **No Windows:**

  ```bash
  gradlew.bat test
  ```

Ao final da execução, o terminal exibirá `BUILD SUCCESSFUL` caso todos os testes passem (verificando tanto os Controllers quanto a lógica de Casos de Uso). Os relatórios de teste ficam salvos no diretório `build/reports/tests/test/`.

## 📍 Endpoints Disponíveis

Com a aplicação rodando, você pode testar as seguintes rotas (via navegador, Postman ou cURL):

### 1. Health Check

Verifica se a API está no ar.

```bash
curl http://localhost:8080/health
```

**Retorno esperado:**

```json
{"status": "ok"}
```

### 2. Listagem de Contas (Mock)

Retorna a lista de contas solidárias direto do repositório em memória.

```bash
curl http://localhost:8080/accounts
```

**Retorno esperado:**

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
