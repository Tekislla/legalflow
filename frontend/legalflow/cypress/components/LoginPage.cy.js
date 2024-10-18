import LoginPage from "@/pages/LoginPage.vue";

describe("LoginPage", () => {
  it("Deve renderizar a página corretamente", () => {
    cy.mount(LoginPage);

    cy.contains("Login").should("be.visible");
    cy.contains("Ainda não tem uma conta?").should("be.visible");
    cy.contains("Cadastre-se").should("be.visible");
  });
});
