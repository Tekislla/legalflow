import { axios } from "src/boot/axios";

class QuadroService {
  salvarQuadro(form) {
    return axios.post("/quadro/", form);
  }

  deletarQuadro(id) {
    return axios.delete(`/quadro/${id}`);
  }
}

export default new QuadroService();
