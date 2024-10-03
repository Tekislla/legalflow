import Vuex from "vuex";

// Função para criar um mock do Vuex Store que espelha a store real
export const createStoreMock = (initialState = {}) => {
  // Simula o `localStorage` com dados de usuário logado
  localStorage.setItem("token", initialState.token || "mocked_token");
  localStorage.setItem(
    "usuario",
    JSON.stringify(
      initialState.usuario || {
        id: 1,
        nome: "Mocked User",
        email: "mocked@example.com",
        role: "ADMIN",
      }
    )
  );
  localStorage.setItem(
    "organizacaoId",
    initialState.organizacaoId || "mocked_organizacao"
  );
  localStorage.setItem(
    "idQuadroAtual",
    initialState.idQuadroAtual || "mocked_quadro"
  );

  return new Vuex.Store({
    state: {
      token: localStorage.getItem("token") || "",
      usuario: JSON.parse(localStorage.getItem("usuario")) || {},
      organizacaoId: localStorage.getItem("organizacaoId") || "",
      idQuadroAtual: localStorage.getItem("idQuadroAtual") || "",
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
      setidQuadroAtual(state, idQuadroAtual) {
        state.idQuadroAtual = idQuadroAtual;
        localStorage.setItem("idQuadroAtual", idQuadroAtual);
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
        // Simula a ação de login
        return new Promise((resolve) => {
          const response = {
            data: {
              token: "mocked_token",
              usuario: {
                id: 1,
                nome: "Mocked User",
                email: "mocked@example.com",
                role: "ADMIN",
              },
              organizacaoId: "mocked_organizacao",
            },
          };
          commit("setToken", response.data.token);
          commit("setUsuario", response.data.usuario);
          commit("setOrganizacaoId", response.data.organizacaoId);
          resolve(response);
        });
      },
      logout({ commit }) {
        commit("logout");
      },
    },
    getters: {
      getUsuario: (state) => state.usuario,
      getRole: (state) => state.usuario.role,
    },
  });
};
