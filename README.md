# Sistema de Gestão de Cobranças

> ⚠️ **Projeto em desenvolvimento** — Funcionalidades estão sendo adicionadas progressivamente.

Sistema web desenvolvido para gerenciar acordos de cobrança de escolas de cursos profissionalizantes, permitindo o controle de clientes, acordos e parcelas de forma organizada e eficiente.

## 🚀 Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.5.11**
- **Spring Security**
- **Spring Data JPA**
- **Thymeleaf**
- **H2 Database** (banco em memória para desenvolvimento)
- **Maven**

## 📋 Funcionalidades

- Cadastro e gerenciamento de **Unidades** (escolas parceiras)
- Cadastro e gerenciamento de **Clientes** com máscara de CPF e telefone
- Busca de clientes por CPF
- Lançamento de **Acordos** vinculados a clientes
- Geração automática de **parcelas** com valor de entrada separado
- **Status automático** do acordo baseado nas parcelas (Em Dia, Em Atraso, Quitado, Cancelado)
- Detecção automática de parcelas vencidas
- Edição de acordos e parcelas
- Cancelamento de acordos

## 🏗️ Arquitetura

O projeto segue a arquitetura em camadas do Spring Boot:

Controller → Service → Repository → Banco de Dados

- **Model** — entidades que representam as tabelas do banco
- **Repository** — comunicação com o banco de dados via Spring Data JPA
- **Service** — regras de negócio
- **Controller** — recebe requisições e retorna as telas
- **Templates** — telas em Thymeleaf

## 📦 Como Executar

### Pré-requisitos

Antes de começar, certifique-se de ter instalado o [Java 17](https://www.oracle.com/java/technologies/downloads/#java17) e o [Git](https://git-scm.com/downloads).

Para verificar se estão instalados, abra o terminal e execute:

    java -version
    git --version

### Passo a Passo

**1. Clone o repositório**

    git clone https://github.com/morgangab/cobranca.git

**2. Acesse a pasta do projeto**

    cd cobranca

**3. Execute o projeto**

No Windows:

    mvnw.cmd spring-boot:run

No Linux/Mac:

    ./mvnw spring-boot:run

**4. Aguarde a inicialização**

Quando aparecer no terminal:

    Started CobrancaApplication in X seconds

**5. Acesse no navegador**

    http://localhost:8080

**6. Faça login**

Usuário: `user`
Senha: gerada automaticamente no terminal, procure por:

    Using generated security password: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx

> **Nota:** O banco de dados H2 é em memória, os dados são apagados ao reiniciar o projeto. Em breve será substituído por um banco de dados persistente.

## 🔮 Próximos Passos

- [ ] Sistema de login com usuários reais
- [ ] Controle de permissões por perfil (Analista, Supervisor, Dono, Operador de Boleto)
- [ ] Banco de dados persistente (PostgreSQL ou MySQL)
- [ ] Relatórios e dashboards
- [ ] Migração do frontend para React

## 👨‍💻 Autor

Desenvolvido por Gabriel
