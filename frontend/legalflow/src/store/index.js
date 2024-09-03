import { createStore } from "vuex";
import { axios } from "src/boot/axios";

export default createStore({
  state: {
    token: localStorage.getItem("token") || "",
    usuario: JSON.parse(localStorage.getItem("usuario")) || {},
    organizacaoId: localStorage.getItem("organizacaoId") || "",
  },
  mutations: {
    setToken(state, token) {
      state.token = token;
      localStorage.setItem("token", token);
    },
    setUsuario(state, usuario) {
      state.usuario = usuario;
      localStorage.setItem("usuario", JSON.stringify(usuario));
    },
    setOrganizacaoId(state, organizacaoId) {
      state.organizacaoId = organizacaoId;
      localStorage.setItem("organizacaoId", organizacaoId);
    },
    logout(state) {
      state.token = "";
      state.usuario = {};
      localStorage.removeItem("token");
      localStorage.removeItem("usuario");
    },
  },
  actions: {
    login({ commit }, authData) {
      return axios.post("/auth/login", authData).then((response) => {
        commit("setToken", response.data.token);
        commit("setUsuario", response.data.usuario);
        commit("setOrganizacaoId", response.data.organizacaoId);
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
