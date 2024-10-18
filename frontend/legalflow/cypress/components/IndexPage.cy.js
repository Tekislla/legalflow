import IndexPage from "@/pages/IndexPage.vue";

describe("IndexPage", () => {
  it("Deve renderizar a página inicial corretamente", () => {
    cy.mount(IndexPage, {
      props: {
        userName: "João",
      },
    });

    cy.contains("Olá").should("be.visible");
    cy.contains("João").should("be.visible");
  });
});
