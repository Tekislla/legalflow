import { createStore } from "vuex";
import { axios } from "@/boot/axios";

export default createStore({
  state: {
    token: localStorage.getItem("token") || "",
    usuario: JSON.parse(localStorage.getItem("usuario")) || {},
    organizacaoId: Number(localStorage.getItem("organizacaoId")) || 0,
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
      const orgId = Number(organizacaoId);
      state.organizacaoId = orgId;
      localStorage.setItem("organizacaoId", orgId);
    },
    logout(state) {
      state.token = "";
      state.usuario = {};
      localStorage.removeItem("token");
      localStorage.removeItem("usuario");
      localStorage.removeItem("organizacaoId");
    },
  },
  actions: {
    login({ commit }, authData) {
      return axios.post("/auth/login", authData).then((response) => {
        commit("setToken", response.data.token);
        commit("setUsuario", {
          id: response.data.usuario.id,
          nome: response.data.usuario.nome,
          email: response.data.usuario.email,
          role: response.data.usuario.role,
        });
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
