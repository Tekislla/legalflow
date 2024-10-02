import MainLayout from "@/layouts/MainLayout.vue";
import { setupMocks } from "../support/mocks";
import { createStoreMock } from "../support/store"; // Mock do Vuex Store

describe("HeaderComponent dentro do MainLayout", () => {
  beforeEach(() => {
    setupMocks();

    cy.mount(MainLayout, {}).as("mountedComponent");
  });

  it("Deve verificar as condições de renderização e depurar variáveis", () => {
    cy.get("@mountedComponent")
      .then(({ wrapper }) => {
        // Define os valores no `data` do MainLayout
        wrapper.setData({
          processosCriados: 5,
          processosEmProgresso: 10,
          processosFinalizados: 3,
          processosArquivados: 2,
          actualQuadroId: 1,
        });

        // Aguarda o Vue processar a atualização e renderizar os elementos
        return wrapper.vm.$nextTick();
      })
      .then(() => {
        // Verifica e loga o estado do data do MainLayout para depuração
        cy.get("@mountedComponent").then(({ wrapper }) => {
          cy.log("Estado atual do data do MainLayout:");
          cy.log(`processosCriados: ${wrapper.vm.processosCriados}`);
          cy.log(`processosEmProgresso: ${wrapper.vm.processosEmProgresso}`);
          cy.log(`actualQuadroId: ${wrapper.vm.actualQuadroId}`);

          // Verifica se o HeaderComponent está corretamente montado
          const headerComponent = wrapper.findComponent({
            name: "HeaderComponent",
          });
          expect(headerComponent.exists()).to.be.true;

          // Log para verificar as props do HeaderComponent
          cy.log("Props do HeaderComponent:");
          cy.log(`actualQuadroId: ${headerComponent.props("actualQuadroId")}`);
          cy.log(`userRole: ${headerComponent.vm.userRole}`);
          cy.log(
            `processosCriados: ${headerComponent.props("processosCriados")}`
          );

          // Verifica se o botão "Novo Usuário" é exibido corretamente
          cy.contains("Novo Usuário").should("be.visible");

          // Verifica se os tabs de processos foram renderizados
          cy.contains("Criado").should("exist");
          cy.contains("Em Progresso").should("exist");
          cy.contains("Finalizado").should("exist");
          cy.contains("Arquivado").should("exist");
        });
      });
  });
});
