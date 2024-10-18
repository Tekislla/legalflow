import DashboardPage from "@/pages/DashboardPage.vue";
import { Quasar } from "quasar";

describe("Teste de renderização do DashboardPage", () => {
  beforeEach(() => {
    cy.intercept("GET", "/**/dashboard/**", {
      statusCode: 200,
      body: {
        totalProcessosEmAberto: 5,
        processosAVencerPrazoSubsidio: [
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
            quadro: { id: 1, nome: "Quadro 1" },
          },
        ],
        processosAVencerPrazoFatal: [
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
            quadro: { id: 1, nome: "Quadro 1" },
          },
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
            quadro: { id: 1, nome: "Quadro 1" },
          },
        ],
        processosAVencer: [],
      },
    }).as("getDashboard");

    cy.mount(DashboardPage, {
      global: {
        plugins: [Quasar],
      },
      props: {
        userName: "João",
      },
    });
  });

  it("Deve renderizar corretamente o DashboardPage", () => {
    cy.contains("Olá").should("be.visible");
    cy.contains("João").should("be.visible");

    cy.contains("Total de processos em aberto").should("be.visible");
    cy.contains("5").should("be.visible");

    cy.contains("Processos a vencer/vencidos (prazo de subsídio)").should(
      "be.visible"
    );
    cy.contains("1").should("be.visible");

    cy.contains("Processos a vencer/vencidos (prazo fatal)").should(
      "be.visible"
    );
    cy.contains("2").should("be.visible");
  });
});
