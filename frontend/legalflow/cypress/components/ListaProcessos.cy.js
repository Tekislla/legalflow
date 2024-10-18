import ListaProcessos from "@/components/ListaProcessos.vue";

describe("ListaProcessos", () => {
  it("Deve renderizar o componente corretamente", () => {
    cy.mount(ListaProcessos);

    cy.contains("Detalhes").should("be.visible");
    cy.get("q-table").should("exist");
  });
});
