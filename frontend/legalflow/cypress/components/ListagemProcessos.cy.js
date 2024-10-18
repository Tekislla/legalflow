import ListagemProcessos from "@/pages/ListagemProcessos.vue";

describe("ListagemProcessos", () => {
  it("Deve renderizar a página de listagem de processos corretamente", () => {
    cy.mount(ListagemProcessos, {
      props: {
        processos: [],
      },
    });

    cy.contains("Detalhes").should("be.visible");
    cy.contains("Novo Processo").should("be.visible");
    cy.contains("Editar Quadro").should("be.visible");

    cy.get("q-table").should("exist");
    cy.get("q-dialog").should("exist");
  });
});
