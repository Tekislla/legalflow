import { axios } from "src/boot/axios";

class AuthService {
  login(credentials) {
    return axios.post(`/auth/login`, credentials);
  }

  cadastro(user) {
    return axios.post(`/auth/cadastro`, user);
  }
}

export default new AuthService();
