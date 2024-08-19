import axios from "axios";

const PROJECTS_API_BASE_URL = "http://localhost:8080/manager/projects";

class ProjectService {
  saveProject(project) {
    return axios.post(PROJECTS_API_BASE_URL, project);
  }

  listProjects() {
    return axios.get(PROJECTS_API_BASE_URL);
  }

  deleteProject(projectId) {
    return axios.delete(PROJECTS_API_BASE_URL + "/" + projectId);
  }
}

export default new ProjectService();
