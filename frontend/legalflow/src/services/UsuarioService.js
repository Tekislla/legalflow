import axios from "../axios";

class UsuarioService {
  getUsuarioInfo() {
    return axios.get("/usuario/");
  }
}

export default new UsuarioService();
