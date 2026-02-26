
# ✂️ Agenda Barbearia API

API RESTful para gerenciamento de agendamentos em barbearias, desenvolvida com **Spring Boot 3.4.3** e **Java 21**. O projeto utiliza **Docker** para padronização de ambiente e **JWT** para segurança stateless.

## 📌 Sobre o Projeto

O sistema resolve o problema de conciliação de agendas entre barbeiros e clientes, permitindo o cadastro de profissionais, usuários e a marcação de serviços de forma segura e documentada.

## 🧱 Tecnologias e Dependências

* **Java 21**
* **Spring Boot 3.4.3**
* **Spring Security & JWT** (Autenticação e Autorização)
* **Spring Data JPA** (Persistência de dados)
* **PostgreSQL** (Banco de dados relacional)
* **Docker & Docker Compose** (Containerização)
* **Lombok** (Produtividade/Código limpo)
* **SpringDoc OpenAPI** (Documentação via Swagger)
* **Bean Validation** (Validação de dados de entrada)

## 🗂 Arquitetura

O projeto segue o padrão de camadas para facilitar a manutenção e testes:

* **Controller**: Exposição de endpoints e captura de requisições.
* **Service**: Camada de lógica de negócio e validações.
* **Repository**: Abstração da comunicação com o PostgreSQL.
* **Security**: Filtros de autenticação e configuração de acesso.

---

## 🚀 Como Rodar Localmente

### Pré-requisitos

* Docker e Docker Compose instalados.

### Passo a Passo

1. **Clone o repositório:**
```bash
git clone https://github.com/seu-usuario/agenda-barbearia.git
cd agenda-barbearia

```


2. **Suba os serviços via Docker:**
```bash
docker-compose up --build

```


*Este comando irá baixar as imagens, compilar o código Java e subir o banco de dados automaticamente.*
3. **Acesse a documentação:**
Abra o navegador em: `http://localhost:8080/swagger-ui/index.html`

---

## 🔐 Autenticação (JWT)

A maioria das rotas requer autenticação.

1. Crie um usuário no endpoint `POST /users`.
2. Gere o token no endpoint `POST /login`.
3. No Swagger, clique em **Authorize** e cole o token precedido pela palavra `Bearer`. Exemplo: `Bearer eyJhbGciOiJIUzI1...`

---

## 📮 Endpoints Principais (Exemplos)

### Login

`POST /login`

```json
{
  "username": "admin",
  "password": "123456"
}

```

### Agendamento

`POST /agendamentos`

```json
{
  "dataHora": "2026-05-15T14:30:00",
  "barbeiroId": 1,
  "clienteId": 2
}

```

---

## 🗄 Diagrama do Banco de Dados

A API utiliza as seguintes entidades principais:

* **User/Role**: Gestão de acessos.
* **Barbeiro**: Cadastro de profissionais.
* **Cliente**: Cadastro de usuários do serviço.
* **Agendamento**: Vincula cliente, barbeiro e horário.

---

## 👤 Usuário de Teste

Para facilitar o primeiro acesso:

* **Usuário:** `admin`
* **Senha:** `admin123`
*(Certifique-se de executar o cadastro inicial se o banco estiver vazio)*

---

Este README está completo? Se precisar, posso adicionar uma seção sobre como rodar os testes unitários com **JUnit 5** e **Mockito**!
