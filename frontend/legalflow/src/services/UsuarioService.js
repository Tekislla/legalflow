import { axios } from "@/boot/axios";

class UsuarioService {
  getUsuarioInfo() {
    return axios.get("/usuario/");
  }

  excluirUsuario(id) {
    return axios.delete(`/usuario/${id}`);
  }
}

export default new UsuarioService();
