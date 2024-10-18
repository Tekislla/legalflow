import ListaQuadros from "@/components/ListaQuadros.vue";

describe("ListaQuadros", () => {
  it("Deve renderizar o componente corretamente", () => {
    cy.mount(ListaQuadros, {
      props: {
        quadro: {
          id: 1,
          nome: "Quadro 1",
          processos: [],
          responsavel: "João",
        },
        idQuadroAtual: 1,
        processosEmAberto: 5,
      },
    });

    cy.contains("Quadro 1").should("be.visible");

    cy.contains("Responsável").should("be.visible");
    cy.contains("João").should("be.visible");

    cy.contains("5").should("be.visible");
    cy.contains("processos em aberto").should("be.visible");
  });
});
