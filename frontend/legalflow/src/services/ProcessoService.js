import { axios } from "src/boot/axios";

class ProcessoService {
  salvarProcesso(form) {
    return axios.post("/processo/", form);
  }

  deletarProcesso(id) {
    return axios.delete(`/processo/${id}`);
  }
}

export default new ProcessoService();
