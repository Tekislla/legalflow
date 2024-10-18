import ModalEditarQuadro from "@/components/ModalEditarQuadro.vue";

describe("ModalEditarQuadro", () => {
  it("Deve renderizar o ModalEditarQuadro corretamente", () => {
    cy.mount(ModalEditarQuadro);

    cy.contains("Editar Quadro").should("be.visible");
    cy.get("q-card").should("exist");
  });
});
