import { boot } from "quasar/wrappers";
import axios from "axios";
import store from "../store/index";

axios.defaults.baseURL = process.env.VUE_APP_API_BASE_URL;

axios.interceptors.request.use(
  (config) => {
    const token = store.state.token;

    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

axios.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    if (error.response.status === 401) {
      store.commit("logout");
      window.location = "/#/auth/login";
    }
    return Promise.reject(error);
  }
);

export default boot(({ app }) => {
  app.config.globalProperties.$axios = axios;
});

export { axios };
