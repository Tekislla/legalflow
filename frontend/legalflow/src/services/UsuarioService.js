import { axios } from "@/boot/axios";

class UsuarioService {
  getUsuarioInfo() {
    return axios.get("/usuario/");
  }

  editarUsuario(usuario) {
    return axios.put("/usuario/", usuario);
  }

  excluirUsuario(id) {
    return axios.delete(`/usuario/${id}`);
  }
}

export default new UsuarioService();
