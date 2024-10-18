import ListagemUsuarios from "@/pages/ListagemUsuarios.vue";
import { Quasar } from "quasar";

describe("ListagemUsuarios", () => {
  it("Deve renderizar a página de listagem de usuários corretamente", () => {
    cy.mount(ListagemUsuarios, {
      global: {
        plugins: [Quasar],
      },
      props: {
        usuarios: [],
        emailUsuarioLogado: "jplazarim@gmail.com",
        userRole: "ADMIN",
      },
      data() {
        return {
          usuarioSelecionado: {
            senha: "",
            confirmaSenha: "",
          },
        };
      },
    });

    cy.contains("Detalhes").should("be.visible");
    cy.get("q-table").should("exist");
    cy.get("q-dialog").should("exist");
  });
});
