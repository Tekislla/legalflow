import api from "@/config/axios";

class AuthService {
  login(credentials) {
    return api.post(`/auth/login`, credentials);
  }

  cadastro(user) {
    return api.post(`/auth/cadastro`, user);
  }
}

export default new AuthService();
