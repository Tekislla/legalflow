import ErrorNotFound from "@/pages/ErrorNotFound.vue";

describe("ErrorNotFound", () => {
  it("Deve renderizar a página de erro 404 corretamente", () => {
    cy.mount(ErrorNotFound);

    cy.contains("404").should("be.visible");
    cy.contains("Oops. Nada por aqui...").should("be.visible");
  });
});
