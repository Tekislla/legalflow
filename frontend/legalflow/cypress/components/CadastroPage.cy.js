import CadastroPage from "@/pages/CadastroPage.vue";

describe("CadastroPage", () => {
  it("Deve renderizar a página corretamente", () => {
    cy.mount(CadastroPage);

    cy.contains("Cadastro").should("be.visible");
    cy.contains("Já tem uma conta?").should("be.visible");
    cy.contains("Faça login").should("be.visible");
  });
});
