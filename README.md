# 📚 LiterAlura

Um projeto Java de console para gerenciamento de um **Catálogo de Livros e Autores**. Este desafio faz parte do programa de formação ONE (Oracle + Alura) e utiliza dados da API [Gutendex](https://gutendex.com/), além de persistência em banco de dados PostgreSQL via Spring Boot.

---

## 🚀 Funcionalidades

O sistema permite que o usuário interaja via terminal com as seguintes opções:

1. Buscar livros por título via API Gutendex  
2. Buscar livros por autor via API Gutendex  
3. Listar todos os livros salvos no banco de dados  
4. Listar autores salvos  
5. Consultar autores vivos em determinado ano  
6. Contar livros por idioma  
7. Sair do sistema  

---

## 🔧 Tecnologias utilizadas

- Java 17+  
- Spring Boot 3.x  
- Spring Data JPA e Hibernate ORM  
- PostgreSQL (banco relacional)  
- Maven (gerenciamento de dependências)  
- HttpClient (Java para consumo de API REST)  
- Gson (manipulação de JSON)  
- Git e GitHub  
- IntelliJ IDEA (ou outra IDE de preferência)  

---

## 🧠 O que aprendi com este projeto

- Consumir APIs REST em Java usando HttpClient  
- Manipular JSON com Gson  
- Configurar e usar Spring Boot para criar aplicações Java  
- Modelar entidades JPA com relacionamentos (ex: Livro e Autor)  
- Usar Spring Data JPA para persistência e consultas no banco PostgreSQL  
- Criar consultas customizadas com `@Query` no Spring Data  
- Estruturar um projeto com camadas claras (model, repository, service, main)  
- Executar lógica inicial com `CommandLineRunner` para testes e demonstrações  
- Gerenciar banco de dados com Hibernate e entender erros comuns (ex: transient instances)  
- Uso de Git para versionamento e publicação do código no GitHub  
- Importância de escrever README claro para apresentação do projeto  
- Melhorar disciplina e foco estudando programação de backend apesar das dificuldades pessoais  

---

## 🗂️ Organização do projeto

- `model/` — Classes principais como `Livro` e `Autor`  
- `repository/` — Interfaces que definem operações no banco (JPA Repositories)  
- `service/` — (Opcional) Lógica de negócio e consumo da API  
- `AppRunner.java` — Classe que executa a inicialização dos dados e testes no console  
- `Main.java` — Ponto de entrada do Spring Boot  
- `resources/application.properties` — Configurações do banco e Spring Boot  

---


## 🤝 Contribuições

Projeto individual como parte do programa ONE da Alura e Oracle. Feedbacks são sempre bem-vindos!  

---

## 📬 Contato

Entre em contato pelo GitHub: (https://github.com/leogonzaga82)  

---

## Licença

Este projeto é aberto para fins educacionais e aprendizado pessoal.
