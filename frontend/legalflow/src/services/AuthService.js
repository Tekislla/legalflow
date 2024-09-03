class AuthService {
  login(credentials) {
    return this.$axios.post(`/auth/login`, credentials);
  }

  cadastro(user) {
    return this.$axios.post(`/auth/cadastro`, user);
  }
}

export default new AuthService();
