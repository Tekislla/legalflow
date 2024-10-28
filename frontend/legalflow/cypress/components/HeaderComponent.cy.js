import HeaderComponent from "@/components/HeaderComponent.vue";

describe("HeaderComponent", () => {
  it("Deve renderizar o componente corretamente", () => {
    cy.mount(HeaderComponent, {
      props: {
        processosCriados: 5,
        processosEmProgresso: 10,
        processosFinalizados: 3,
        processosArquivados: 2,
        idQuadroAtual: 1,
        userRole: "ADMIN",
      },
    });

    cy.contains("5").should("be.visible");
    cy.contains("10").should("be.visible");
    cy.contains("3").should("be.visible");
    cy.contains("2").should("be.visible");
  });
});
