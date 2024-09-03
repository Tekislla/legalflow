import axios from "../axios";

class QuadroService {
  salvarQuadro(form) {
    return axios.post("/quadro/", form);
  }
}

export default new QuadroService();
