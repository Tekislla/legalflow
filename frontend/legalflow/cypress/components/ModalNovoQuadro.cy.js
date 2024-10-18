import ModalNovoQuadro from "@/components/ModalNovoQuadro.vue";

describe("ModalNovoQuadro", () => {
  it("Deve renderizar o componente corretamente", () => {
    cy.mount(ModalNovoQuadro);

    cy.contains("Novo Quadro").should("be.visible");
    cy.get("q-card").should("exist");
  });
});
