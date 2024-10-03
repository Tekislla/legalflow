// component.js (cypress/support/component.js)

import { mount } from "cypress/vue";
import { createStoreMock } from "../support/store"; // Mock da store que você já definiu
import { Quasar } from "quasar"; // Importa o Quasar caso sua aplicação use
import "quasar/dist/quasar.css"; // Importa o CSS do Quasar para garantir o estilo

// Registra o comando mount do Cypress e injeta a store e o Quasar globalmente
Cypress.Commands.add("mount", (component, options = {}) => {
  const initialState = {
    token: "mocked_token",
    usuario: {
      id: 1,
      nome: "Mocked User",
      email: "mocked@example.com",
      role: "ADMIN",
    },
    organizacaoId: "mocked_organizacao",
    idQuadroAtual: "1",
  };

  const store = createStoreMock(initialState); // Cria a store mockada

  return mount(component, {
    global: {
      plugins: [store, Quasar], // Injeta o Vuex store e o Quasar em todos os componentes montados
      ...options.global, // Permite a opção de sobrescrever ou adicionar plugins ao montar
    },
    ...options,
  });
});
