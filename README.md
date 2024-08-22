# LegalFlow

LegalFlow é uma plataforma de gestão de processos judiciais que funciona como um kanban, permitindo o acompanhamento de processos através de diferentes status. O sistema é projetado para ser utilizado por escritórios de advocacia e departamentos jurídicos para gerenciar seus casos de forma eficiente.

## Funcionalidades

- **Cadastro de Usuários**: 
  - Um usuário administrador pode se cadastrar e, dentro de sua conta, cadastrar outros usuários (administradores ou usuários comuns).
  
- **Gestão de Processos**:
  - O administrador pode cadastrar novos processos, incluindo detalhes como número do processo, nome, cliente, responsável, observações e anexos (PDF).
  - Atribuição de processos a usuários responsáveis.

- **Acompanhamento de Status**:
  - Os processos podem ser acompanhados através de status como: Criado, Em Andamento, Finalizado, Cancelado, etc.
  - Usuários responsáveis podem alterar o status dos processos sob sua responsabilidade.

## Tecnologias Utilizadas

- **Backend**: 
  - Java com Spring Boot
- **Frontend**: 
  - Vue.js com Quasar Framework
- **Banco de Dados**: 
  - PostgreSQL
- **Conteinerização**: 
  - Docker

## Requisitos

- **Java 17+**
- **Node.js 16+**
- **Docker**
- **PostgreSQL**

## Como Executar o Projeto

### Backend

1. Clone o repositório:
   
   ```
   git clone https://github.com/usuario/legalflow.git
   ```
   
2. Navegue até a pasta do backend:

   ```
   cd legalflow/backend
   ```


3. Renomeie o arquivo application.yml.example dentro de src/main/resources para application.yml, configure o banco de dados PostgreSQL e preencha as configurações necessárias (o banco estará pré-configurado para rodar local na porta 5432).

4. Execute o projeto:

    ```
    ./mvnw spring-boot:run
    ```

### Frontend

1. Navegue até a pasta do frontend:

    ```
    cd legalflow/frontend
    ```

2. Instale as dependências:

    ```
    npm install
    ```
    
3. Execute o projeto:
   
    ```
    quasar dev
    ```


### Docker

1. Navegue até a raiz do projeto e execute:

    ```
    docker-compose up --build
    ```
    
Isso irá subir o backend, frontend e banco de dados em contêineres separados.

## Contribuição

Contribuições são bem-vindas! Sinta-se à vontade para abrir issues e pull requests.

## Licença

Este projeto está licenciado sob a MIT License.
