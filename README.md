# Solari - Order Service

Este microsserviço é o **orquestrador principal** do sistema **Solari**, responsável por gerenciar e coordenar os pedidos, incluindo validações, comunicação com outros microsserviços e processamento de ordens. Ele faz parte do sistema de gerenciamento de pedidos do projeto **Solari**, desenvolvido no **Tech Challenge - Fase 4** da pós-graduação em Arquitetura e Desenvolvimento Java - FIAP.

---

## 🧩 Tecnologias Utilizadas

- **Java 21**: Linguagem principal do projeto.
- **Spring Boot**: Framework para criação de aplicações Java.
- **Maven**: Gerenciador de dependências e build.
- **Kafka**: Sistema de mensageria para comunicação assíncrona.
- **JPA / Hibernate**: Persistência de dados.
- **Docker**: Containerização da aplicação.
- **JaCoCo**: Ferramenta para análise de cobertura de testes.
- **JUnit 5 + Mockito**: Frameworks para testes unitários e mocks.
- **Resilience4j**: Implementação de padrões de resiliência como Retry e Circuit Breaker.

---

## 🧱 Estrutura do Projeto

O projeto segue a Clean Architecture, dividindo responsabilidades em camadas bem definidas:

- **application**: Contém os casos de uso e regras de negócio.
- **domain**: Representa as entidades e objetos de domínio.
- **infrastructure**: Implementações de gateways, repositórios, controladores e configurações.
- **tests**: Testes unitários e de integração.

---

## 🚀 Como executar localmente

### Pré-requisitos

- Java 21+
- Maven
- Docker
- Kafka (local ou em container)

### Passos

1. Clonar o repositório:

   git clone https://github.com/BrunaCasagrande/solari-order-service.git  
   cd solari-order-service

2. Configurar o Kafka no arquivo application.properties (se necessário).

3. Executar o projeto com Maven:

   mvn spring-boot:run

---

## 📌 Endpoints Principais

### Pedidos

- **POST** /solari/v1/orders  
  Cria um novo pedido.

- **GET** /solari/v1/orders/{id}  
  Busca um pedido pelo ID.

- **PUT** /solari/v1/orders/{id}  
  Atualiza os dados de um pedido.

- **DELETE** /solari/v1/orders/{id}  
  Remove um pedido pelo ID.

---

## ✅ Testes

Para executar os testes e gerar o relatório de cobertura com JaCoCo:

1. Executar os testes:

   mvn test

2. Gerar o relatório de cobertura:

   mvn jacoco:report

3. Acessar o relatório em:  
   file:///C:/solari/solari-order-service/target/site/jacoco/index.html

---

## 🐳 Executando com Docker

### Build da imagem Docker:

docker build -t solari-order-service .

### Executar o container:

docker run -p 8082:8082 solari-order-service

### Acessar a aplicação:

http://localhost:8082/solari/v1/orders

---

## 👩‍💻 Autor

Desenvolvido por **Bruna Casagrande RM: 359536** como parte do projeto **Solari**.
