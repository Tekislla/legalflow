import { axios } from "@/boot/axios";

class ProcessoService {
  salvarProcesso(form) {
    return axios.post("/processo/", form, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  }

  deletarProcesso(id) {
    return axios.delete(`/processo/${id}`);
  }
}

export default new ProcessoService();
