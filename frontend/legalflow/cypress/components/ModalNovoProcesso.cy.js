import ModalNovoProcesso from "@/components/ModalNovoProcesso.vue";

describe("ModalNovoProcesso", () => {
  it("Deve renderizar o ModalNovoProcesso corretamente", () => {
    cy.mount(ModalNovoProcesso);

    cy.contains("Novo Processo").should("be.visible");
    cy.get("q-card").should("exist");
  });
});
