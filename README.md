
# 📝 To-Do List Pro (Integrada com Telegram e E-mail)

Este é um sistema **Full-Stack** de gerenciamento de tarefas desenvolvido para demonstrar o uso de **notificações assíncronas** e integração com APIs externas.

O projeto vai além de um CRUD simples, implementando um sistema de **alertas em tempo real** via Telegram e E-mail sempre que uma tarefa é criada ou concluída, utilizando processamento em background (`@Async`) para garantir alta performance.

-----

## ✨ Funcionalidades Principais

  * **📋 Gerenciamento de Tarefas:** Criação, listagem, conclusão e exclusão de tarefas.
  * **⚡ Notificações Assíncronas:** O sistema envia alertas sem travar a interface do usuário.
  * **🤖 Integração com Telegram Bot:**
      * Vínculo automático de conta (sem necessidade de digitar ID manualmente).
      * Notificações formatadas com Markdown (Negrito, Emojis).
  * **📧 Poderoso Sistema de E-mail (Spring Mail):**
    * Utiliza a biblioteca `spring-boot-starter-mail` para conexão SMTP robusta.
    * Envio de alertas detalhados (com formatação de dados) sempre que uma tarefa é criada ou concluída.
    * Configuração segura utilizando **Senhas de App** do Google.
  * **⚙️ Configuração Dinâmica:** Painel de configuração no Front-end para definir e-mail e Telegram em tempo de execução (salvos no banco).
  * **🎨 Front-end Moderno:** Interface responsiva, filtros de status (Pendentes/Concluídos) e feedback visual.

-----

## 🏗️ Arquitetura e Tecnologias

O projeto segue a **Arquitetura em Camadas** (3-Tier Architecture) para desacoplamento e organização:

### Back-end (Spring Boot)

  * **Controller:** Expõe endpoints REST (`@RestController`).
  * **Service:** Contém a regra de negócio e orquestra as notificações.
  * **Repository:** Camada de persistência com **Spring Data JPA**.
  * **Model/DTO:** Uso de **Java Records** para DTOs (Data Transfer Objects) imutáveis.
  * **Async:** Uso da anotação **@Async** para disparar e-mails e mensagens do Telegram em threads separadas.
  * **Banco de Dados:** H2 Database (Em memória) para facilidade de testes.

### Front-end

  * **Vanilla JS:** Lógica de conexão com API usando `fetch` e `async/await`.
  * **CSS Moderno:** Variáveis CSS, Flexbox e Grid Layout.

-----

## 🚀 Como Rodar o Projeto

### Pré-requisitos

  * Java 21 instalado.
  * Maven instalado.
  * Uma conta no Gmail (com Senha de App gerada).
  * Um Bot no Telegram (criado via @BotFather).

### 1\. Configuração do Backend

Abra o arquivo `src/main/resources/application.properties` e configure suas credenciais:

```properties
# Banco de Dados
spring.datasource.url=jdbc:h2:mem:todolistdb
spring.datasource.username=sa
spring.datasource.password=

# Configuração de E-mail (Gmail)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=SEU_EMAIL@gmail.com
# Use a SENHA DE APP gerada (16 letras), não sua senha pessoal!
spring.mail.password=xxxx xxxx xxxx xxxx

# Configuração do Telegram
telegram.bot.token=SEU_TOKEN_DO_BOTFATHER_AQUI
# O Chat ID é obtido dinamicamente pelo sistema, não precisa configurar aqui.
```

### 2\. Executando a Aplicação

No terminal, na raiz do projeto, execute:

```bash
mvn spring-boot:run
```

O servidor iniciará na porta `8080`.

### 3\. Acessando o Front-end

Basta abrir o arquivo `index.html` em qualquer navegador moderno.

-----

## 🧪 Como Testar a Integração com Telegram

Um dos diferenciais do projeto é o vínculo automático ("Magic Link") com o Telegram. Siga os passos:

1.  Abra o Front-end.
2.  No painel de configurações, clique em **"1. Abrir Bot"**.
3.  No Telegram, clique em **"Começar"** (ou envie um "Oi" para o bot).
4.  Volte ao site e clique em **"2. Vincular"**.
5.  O sistema irá buscar a última mensagem recebida pelo bot, capturar seu ID e salvar no banco de dados.
6.  🎉 Pronto\! Crie uma nova tarefa e veja a notificação chegar no seu celular.

-----

## 🔌 Documentação da API (Endpoints)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `GET` | `/tarefas` | Lista todas as tarefas. |
| `POST` | `/tarefas` | Cria uma nova tarefa e dispara notificações. |
| `PUT` | `/tarefas/{id}` | Atualiza status (concluir/reabrir). |
| `DELETE` | `/tarefas/{id}` | Remove uma tarefa. |
| `PUT` | `/config/email` | Define o e-mail de destino. |
| `POST` | `/config/telegram/vincular` | Vincula o último usuário do Telegram. |

-----

## 📸 Screenshots

### Imagem web
<img width="601" height="655" alt="image" src="https://github.com/user-attachments/assets/31edeb1b-92a1-4d64-8014-f22fa80f8c63" />

### Imagem Telegram
<img width="700" height="309" alt="image" src="https://github.com/user-attachments/assets/91a43759-19c1-45f0-940c-a88686575c98" />

### Imagem e-mail
<img width="402" height="302" alt="image" src="https://github.com/user-attachments/assets/f66c0a64-1055-41de-a9ba-e08e867a1d03" />

-----

## 👨‍💻 Autor

Desenvolvido por **Pedro Lucas Saraiva Cordeiro** como parte do projeto de Tec. Integração de Sistemas.
