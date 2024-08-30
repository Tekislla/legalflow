import axios from "axios";

const TASKS_API_BASE_URL = "http://localhost:8080/legalflow/tasks";

class TaskService {
  saveTask(task) {
    return axios.post(TASKS_API_BASE_URL, task);
  }

  listTasks() {
    return axios.get(TASKS_API_BASE_URL);
  }

  deleteTask(taskId) {
    return axios.delete(TASKS_API_BASE_URL + "/" + taskId);
  }

  listTasksByProject(projectId) {
    return axios.get(TASKS_API_BASE_URL + "/project/" + projectId);
  }
}

export default new TaskService();
