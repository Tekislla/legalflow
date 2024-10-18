import ModalNovoUsuario from "@/components/ModalNovoUsuario.vue";

describe("ModalNovoUsuario", () => {
  it("Deve renderizar o ModalNovoUsuario corretamente", () => {
    cy.mount(ModalNovoUsuario);

    cy.contains("Novo Usuário").should("be.visible");
    cy.get("q-card").should("exist");
  });
});
