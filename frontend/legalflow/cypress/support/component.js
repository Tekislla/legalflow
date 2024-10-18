import { mount } from "cypress/vue";
import { Quasar } from "quasar";
import "quasar/dist/quasar.css";

Cypress.Commands.add("mount", (component, options = {}) => {
  return mount(component, {
    global: {
      plugins: [Quasar],
      ...options.global,
    },
    ...options,
  });
});
