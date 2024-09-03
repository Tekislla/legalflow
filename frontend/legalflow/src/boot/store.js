// src/boot/store.js
import { boot } from "quasar/wrappers";
import store from "src/store"; // Importe o Vuex store

export default boot(({ app }) => {
  // Registre o Vuex store com a instância do Vue
  app.use(store);
});
