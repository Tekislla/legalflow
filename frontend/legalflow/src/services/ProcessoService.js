import { axios } from "@/boot/axios";

class ProcessoService {
  salvarProcesso(form) {
    return axios.post("/processo/", form, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  }

  editarProcesso(form) {
    return axios.put("/processo/", form);
  }

  getDashboardInfo(organizacaoId) {
    return axios.get(`/processo/dashboard/${organizacaoId}`);
  }

  baixarProcesso(id) {
    return axios.get(`/processo/download/${id}`, {
      responseType: "blob",
    });
  }

  deletarProcesso(id) {
    return axios.delete(`/processo/${id}`);
  }
}

export default new ProcessoService();
