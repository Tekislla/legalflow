// cypress/components/HeaderComponent.cy.js
import HeaderComponent from "../../src/components/HeaderComponent.vue";
import { mount } from "cypress/vue";

describe("HeaderComponent", () => {
  it("renders correctly", () => {
    mount(HeaderComponent, {
      props: {
        processosCriados: 5,
        processosEmProgresso: 2,
        processosFinalizados: 1,
        processosArquivados: 3,
        actualQuadroId: 1,
      },
    });

    cy.get("q-toolbar-title").should("contain", "LegalFlow");
    cy.get(".header-btn").should("have.length", 2); // Botões "Novo Usuário" e "Novo Quadro"
  });

  it("emits event on button click", () => {
    const onToggleLeftDrawer = cy.stub();
    const onAbrirModalNovoUsuario = cy.stub();
    const onAbrirModalNovoQuadro = cy.stub();

    mount(HeaderComponent, {
      props: {
        processosCriados: 5,
        processosEmProgresso: 2,
        processosFinalizados: 1,
        processosArquivados: 3,
        actualQuadroId: 1,
      },
      listeners: {
        "toggle-left-drawer": onToggleLeftDrawer,
        "abrir-modal-novo-usuario": onAbrirModalNovoUsuario,
        "abrir-modal-novo-quadro": onAbrirModalNovoQuadro,
      },
    });

    cy.get("q-btn").first().click();
    cy.get("q-btn").eq(1).click();

    expect(onToggleLeftDrawer).to.have.been.calledOnce;
    expect(onAbrirModalNovoUsuario).to.have.been.calledOnce;
    expect(onAbrirModalNovoQuadro).to.have.been.calledOnce;
  });
});
