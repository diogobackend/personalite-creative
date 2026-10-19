# Personalité Creative Backend

**API REST para autenticação, gerenciamento de usuários e gestão comercial e financeira da Personalité Creative.**

## 1. Visão geral

O **Personalité Creative Backend** é o sistema responsável por gerenciar as operações administrativas da Personalité Creative, empresa especializada em produtos e presentes personalizados.

A aplicação tem como objetivo centralizar o gerenciamento de usuários, produtos, vendas, custos, despesas operacionais, estoque e resultados financeiros.

O desenvolvimento será realizado de forma incremental, priorizando uma arquitetura organizada, segura e de fácil manutenção, sem a necessidade de microsserviços ou infraestrutura complexa.

**Repositório:** https://github.com/diogobackend/personalite-backend

## 2. Funcionalidades

### Implementadas

**Autenticação e gerenciamento de usuários**

- Cadastro de usuários.
- Login utilizando e-mail ou username e senha.
- Autenticação com JWT.
- Criptografia de senhas com BCrypt.
- Consulta de usuários por identificador ou dados cadastrais.
- Listagem paginada de usuários.
- Filtros e ordenação de resultados.
- Atualização parcial de informações cadastrais.
- Exclusão de usuários conforme permissões.
- Controle de acesso baseado em perfis.
- Alteração administrativa de perfil e status.
- Validação de dados e prevenção de duplicidades.

### Planejadas

**Gerenciamento de produtos**

- Cadastro, edição, consulta e exclusão de produtos.
- Definição do preço unitário.
- Definição de preços diferenciados conforme a quantidade.
- Registro do custo de produção.
- Organização e consulta do catálogo de produtos.

**Gerenciamento de vendas**

- Registro de vendas.
- Associação de clientes e produtos.
- Inclusão de múltiplos produtos em uma venda.
- Quantidade e preço de cada item.
- Cálculo do valor total.
- Registro da forma de pagamento.
- Acompanhamento das vendas realizadas.
- Cálculo de custos e resultados por venda.

**Gerenciamento financeiro**

- Registro de receitas provenientes das vendas.
- Registro de custos de produção.
- Registro de despesas operacionais, como transporte e outros gastos.
- Consulta de resultados por período.
- Relatórios de faturamento.
- Relatórios de custos e despesas.
- Apuração do lucro líquido.

O lucro líquido será calculado conforme a seguinte regra:

**Lucro líquido = Receita de vendas − Custos de produção − Despesas operacionais**

**Controle de estoque**

- Cadastro e acompanhamento de quantidades disponíveis.
- Atualização das quantidades de produtos.
- Controle de entradas e saídas.
- Consulta de disponibilidade.
- Identificação de produtos com estoque baixo.

## 3. Tecnologias

| Categoria | Tecnologia |
|---|---|
| Linguagem | Kotlin 2.4.20 |
| Plataforma | Java 25 |
| Framework | Spring Boot 4.0.8 |
| API | Spring Web MVC, REST, JSON |
| Segurança | Spring Security, JWT, BCrypt |
| Persistência | PostgreSQL, Spring Data JPA, Hibernate |
| Migrações | Flyway |
| Documentação | OpenAPI, Swagger UI |
| Testes | JUnit 5, MockK |
| Cobertura | JaCoCo |
| Padronização | ktlint |
| Build | Gradle |
| Containerização | Docker |

## 4. Arquitetura

A aplicação utiliza **arquitetura hexagonal (Ports & Adapters)** para separar as regras de negócio dos detalhes de implementação.

### Organização das responsabilidades

**Domain**

Contém entidades, objetos de valor, enums e regras de negócio.

**Use Cases**

Implementa os casos de uso responsáveis pelas operações da aplicação.

**Ports**

Define os contratos de entrada e saída utilizados pelos casos de uso.

**Adapters**

Implementa a comunicação com recursos externos, como endpoints REST e persistência PostgreSQL.

**Configuration**

Centraliza as configurações da aplicação, incluindo segurança, autenticação e componentes do Spring.

### Princípios adotados

- SOLID.
- Clean Code.
- Separação de responsabilidades.
- Inversão de dependências.
- Validação de dados.
- Tratamento centralizado de exceções.
- Testes automatizados.
- Organização por contexto de negócio.

A aplicação será mantida como um **backend único e modular**, evitando complexidade desnecessária.

## 5. Módulos do sistema

| Módulo | Responsabilidade | Situação |
|---|---|---|
| Autenticação | Login, JWT e controle de acesso | Implementado |
| Usuários | Cadastro e gerenciamento de usuários | Implementado |
| Produtos | Catálogo, preços e custos | Planejado |
| Vendas | Registro e acompanhamento de vendas | Planejado |
| Financeiro | Faturamento, custos, despesas e lucro | Planejado |
| Estoque | Controle de quantidades e movimentações | Planejado |

## 6. Autenticação e autorização

A autenticação utiliza Spring Security e JWT.

### Fluxo de autenticação

1. O usuário informa e-mail ou login e senha.
2. O backend valida as credenciais.
3. Após a autenticação, o backend retorna um token JWT.
4. O frontend utiliza o token para acessar operações protegidas.
5. O Spring Security valida o token e as permissões do usuário.

### Perfis de acesso

O módulo de autenticação foi inicialmente reaproveitado de outro projeto e ainda possui os perfis:

- `CANDIDATE`
- `SCHOOL_ADMIN`
- `PLATFORM_ADMIN`

Esses perfis deverão ser adaptados às necessidades administrativas da Personalité Creative.

O cadastro público atualmente atribui automaticamente `CANDIDATE`, enquanto alterações de perfil e status são restritas ao administrador da plataforma.

A definição definitiva dos perfis de acesso será realizada conforme as necessidades do sistema.

## 7. API REST

### Endpoints implementados

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/users` | Cadastrar usuário |
| POST | `/login` | Autenticar usuário |
| GET | `/users` | Listar usuários |
| GET | `/users/id` | Consultar usuário |
| PATCH | `/users/{userId}` | Atualizar usuário |
| DELETE | `/users/{userId}` | Excluir usuário |
| PATCH | `/users/{userId}/access` | Alterar perfil e status |

### Swagger

**Swagger UI**

http://localhost:8080/swagger-ui.html

**OpenAPI**

http://localhost:8080/v3/api-docs

Os endpoints dos módulos de produtos, vendas, financeiro e estoque serão definidos durante suas respectivas implementações.

## 8. Modelo de usuário

| Campo | Descrição |
|---|---|
| `userId` | Identificador UUID |
| `name` | Nome completo |
| `email` | E-mail |
| `login` | Nome de usuário |
| `passwordHash` | Senha criptografada |
| `role` | Perfil de acesso |
| `status` | Status do usuário |
| `phone` | Telefone |
| `cpf` | CPF |
| `rg` | RG |
| `address` | Endereço |
| `birthDate` | Data de nascimento |
| `createdAt` | Data de criação |
| `updatedAt` | Data de atualização |

### Regras

- E-mail e login devem respeitar as validações de formato e unicidade.
- Senhas são armazenadas utilizando BCrypt.
- A senha não é retornada nas respostas da API.
- CPF, RG e data de nascimento são opcionais no cadastro.
- A data de nascimento não pode ser futura.
- Alterações de perfil e status exigem autorização administrativa.
- A atualização cadastral respeita as restrições definidas no domínio.

## 9. Banco de dados

A aplicação utiliza PostgreSQL como banco de dados relacional.

**Spring Data JPA e Hibernate** são utilizados para persistência e consultas.

**Flyway** é responsável pelo versionamento e execução das migrações.

A configuração `ddl-auto: validate` garante que o Hibernate valide a estrutura do banco sem gerar automaticamente alterações no esquema.

### Ambientes

**Desenvolvimento**

PostgreSQL executado localmente.

**Produção**

PostgreSQL hospedado no Neon, com credenciais configuradas por variáveis de ambiente.

## 10. Configurações

Arquivo principal:

`src/main/resources/application.yml`

Variáveis de ambiente utilizadas:

| Variável | Descrição |
|---|---|
| `DATABASE_URL` | URL JDBC do PostgreSQL |
| `DATABASE_USERNAME` | Usuário do banco |
| `DATABASE_PASSWORD` | Senha do banco |
| `JWT_SECRET` | Chave de assinatura JWT |
| `PORT` | Porta HTTP da aplicação |

As credenciais de produção não devem ser armazenadas diretamente no código ou versionadas no Git.

## 11. Execução local

### Pré-requisitos

- JDK 25.
- Docker.
- Git.

### Clonar repositório

```bash
git clone https://github.com/diogobackend/personalite-backend.git
cd personalite-backend
```

### Iniciar PostgreSQL local

Caso o projeto mantenha o arquivo Docker Compose do banco:

```bash
docker compose up -d
```

### Executar aplicação

**Windows**

```powershell
.\gradlew.bat bootRun
```

**Linux/macOS**

```bash
./gradlew bootRun
```

A API ficará disponível em:

http://localhost:8080

## 12. Docker e publicação

A aplicação poderá ser executada em contêiner Docker.

### Ambiente de produção planejado

| Componente | Plataforma |
|---|---|
| Backend | Render |
| Banco de dados | Neon PostgreSQL |
| Frontend | Vercel |

O backend utilizará variáveis de ambiente para conexão com o banco e configuração do JWT.

A comunicação entre frontend e backend será realizada por HTTPS, com CORS configurado para os domínios autorizados.

A publicação não exige Kafka, RabbitMQ, Kubernetes ou infraestrutura distribuída.

## 13. Testes e qualidade

O projeto utiliza JUnit 5 e MockK para testes automatizados.

### Executar testes

```powershell
.\gradlew.bat test
```

### Validar formatação

```powershell
.\gradlew.bat ktlintCheck
```

### Corrigir formatação

```powershell
.\gradlew.bat ktlintFormat
```

### Gerar relatório de cobertura

```powershell
.\gradlew.bat jacocoTestReport
```

### Verificar cobertura

```powershell
.\gradlew.bat jacocoTestCoverageVerification
```

### Build

```powershell
.\gradlew.bat clean build
```

A configuração atual do Gradle estabelece uma verificação de cobertura mínima de 100% para as classes incluídas na análise.

## 14. Segurança

Medidas implementadas:

- Autenticação JWT.
- Armazenamento de senhas com BCrypt.
- Autorização baseada em perfis.
- Controle de acesso aos dados de usuários.
- Validação de requisições.
- Tratamento de erros HTTP.

### Melhorias futuras

- Recuperação de senha.
- Logout com gerenciamento adequado de sessão.
- Renovação de tokens, caso necessária.
- Proteção adicional contra tentativas excessivas de login.
- Adequação definitiva dos perfis de acesso.
- Proteção das rotas autenticadas do frontend.

## 15. Frontend

O frontend é desenvolvido separadamente utilizando:

- Next.js.
- React.
- TypeScript.
- Tailwind CSS.
- Axios.
- React Hook Form.
- Zod.

### Páginas iniciais

| Rota | Descrição |
|---|---|
| `/cadastro` | Cadastro de usuários |
| `/login` | Autenticação |
| `/home` | Dashboard administrativo |

O frontend consome os endpoints REST disponibilizados pelo backend.

A URL da API é configurada por variável de ambiente, permitindo alternar entre desenvolvimento e produção.

## 16. Roadmap

### Etapa 1 — Usuários e autenticação

- [x] Cadastro de usuários.
- [x] Login com JWT.
- [x] Consulta de usuários.
- [x] Atualização cadastral.
- [x] Exclusão de usuários.
- [x] Controle de acesso.
- [x] Integração inicial com frontend.
- [ ] Persistência segura da sessão no frontend.
- [ ] Adaptação dos perfis de acesso.

### Etapa 2 — Produtos

- [ ] Cadastro de produtos.
- [ ] Definição de preços unitários.
- [ ] Preços por quantidade.
- [ ] Registro de custos de produção.
- [ ] Consulta e manutenção do catálogo.

### Etapa 3 — Vendas

- [ ] Registro de vendas.
- [ ] Associação de clientes.
- [ ] Inclusão de múltiplos produtos.
- [ ] Registro de quantidades e valores.
- [ ] Formas de pagamento.
- [ ] Cálculo dos resultados por venda.

### Etapa 4 — Relatórios financeiros

- [ ] Relatório de faturamento.
- [ ] Relatório de custos.
- [ ] Relatório de lucro líquido.
- [ ] Filtros por período.
- [ ] Dashboard com indicadores financeiros.

### Etapa 5 — Despesas operacionais

- [ ] Cadastro de despesas.
- [ ] Classificação dos gastos.
- [ ] Consulta por período.
- [ ] Integração com os resultados financeiros.

### Etapa 6 — Estoque

- [ ] Controle de quantidades.
- [ ] Registro de entradas e saídas.
- [ ] Atualização de estoque.
- [ ] Consulta de disponibilidade.

---

**Personalité Creative Backend**

Sistema de gestão comercial e financeira para produtos personalizados.