export const setupMocks = () => {
  // Mock da requisição de usuário (por exemplo, para o HeaderComponent)
  cy.intercept("GET", "**/usuario/", {
    statusCode: 200,
    body: [
      {
        id: 1,
        nome: "Usuario 1",
        email: "usuario1@example.com",
        quadros: [
          {
            id: 1,
            nome: "Quadro 1",
            processos: [
              {
                id: 1,
                nome: "Processo 1",
                numero: "12345",
                autor: "Autor 1",
                reu: "Reu 1",
                descricao: "Descrição do processo 1",
                status: "Em andamento",
                prazoSubsidio: "2024-01-15",
                prazoFatal: "2024-02-01",
                arquivo: "arquivo1.pdf",
              },
              {
                id: 2,
                nome: "Processo 2",
                numero: "67890",
                autor: "Autor 2",
                reu: "Reu 2",
                descricao: "Descrição do processo 2",
                status: "Concluído",
                prazoSubsidio: "2024-02-20",
                prazoFatal: "2024-03-10",
                arquivo: "arquivo2.pdf",
              },
            ],
          },
          {
            id: 2,
            nome: "Quadro 2",
            processos: [],
          },
        ],
      },
      {
        id: 2,
        nome: "Usuario 2",
        email: "usuario2@example.com",
        quadros: [
          {
            id: 3,
            nome: "Quadro 3",
            processos: [
              {
                id: 3,
                nome: "Processo 3",
                numero: "54321",
                autor: "Autor 3",
                reu: "Reu 3",
                descricao: "Descrição do processo 3",
                status: "Em andamento",
                prazoSubsidio: "2024-03-15",
                prazoFatal: "2024-04-01",
                arquivo: "arquivo3.pdf",
              },
            ],
          },
        ],
      },
    ],
  }).as("getUsuario");

  // Mock da requisição de quadros (para o ModalNovoQuadro, etc)
  cy.intercept("GET", "/api/quadros", {
    statusCode: 200,
    body: [
      { id: 1, nome: "Quadro 1" },
      { id: 2, nome: "Quadro 2" },
    ],
  }).as("getQuadros");

  // Mock da criação de um novo quadro
  cy.intercept("POST", "/api/quadros", {
    statusCode: 201,
    body: { id: 3, nome: "Novo Quadro" },
  }).as("createQuadro");

  // Mock da criação de um novo usuário
  cy.intercept("POST", "/api/usuarios", {
    statusCode: 201,
    body: { id: 123, name: "Novo Usuário" },
  }).as("createUsuario");

  // Adicionar outros mocks conforme necessário
  // Exemplo:
  cy.intercept("GET", "/api/processos", {
    statusCode: 200,
    body: [
      { id: 1, nome: "Processo 1" },
      { id: 2, nome: "Processo 2" },
    ],
  }).as("getProcessos");
};
