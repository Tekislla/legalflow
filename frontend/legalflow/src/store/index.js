import { createStore } from "vuex";
import axios from "src/axios.js";

export default createStore({
  state: {
    token: localStorage.getItem("token") || "",
    usuario: {},
  },
  mutations: {
    setToken(state, token) {
      state.token = token;
      localStorage.setItem("token", token);
    },
    setUsuario(state, usuario) {
      state.usuario = usuario;
    },
    logout(state) {
      state.token = "";
      state.user = {};
      localStorage.removeItem("token");
    },
  },
  actions: {
    login({ commit }, authData) {
      return axios.post("/auth/login", authData).then((response) => {
        commit("setToken", response.data.token);
        commit("setUsuario", response.data.usuario);
      });
    },
    cadastrar({ commit }, userData) {
      return axios.post("/auth/cadastro", userData);
    },
    logout({ commit }) {
      commit("logout");
    },
  },
});
