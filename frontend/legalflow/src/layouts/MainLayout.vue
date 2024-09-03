<!-- -->
<template>
  <q-layout view="hHh lpR fFf">
    <header-component
      :created-tasks="getTaskListSize(createdTasks)"
      :in-progress-tasks="getTaskListSize(inProgressTasks)"
      :done-tasks="getTaskListSize(doneTasks)"
      :canceled-tasks="getTaskListSize(canceledTasks)"
      :actual-quadro-id="actualQuadroId"
      @toggle-left-drawer="toggleLeftDrawer()"
      @update:tab="tab = $event"
      @open-modal-novo-usuario="openModalNovoUsuario()"
      @open-modal-novo-quadro="openModalNovoQuadro()"
      @update:projectModalOpen="projectModalOpen = $event"
    />

    <q-drawer v-model="leftDrawerOpen" show-if-above bordered>
      <q-list>
        <q-item clickable @click="this.actualQuadroId = null">
          <q-item-section avatar> Home </q-item-section>
        </q-item>
        <q-expansion-item label="Quadros">
          <projects-list
            v-for="quadro in quadros"
            :key="quadro"
            v-bind:quadro="quadro"
            :quadros="quadros"
            :actual-quadro-id="actualQuadroId"
            @set-quadro-id="setQuadroId(quadro.id)"
          />
          <q-item
            clickable
            @click="openModalNovoQuadro()"
            v-show="this.projects.length === 0"
          >
            <q-item-section>
              <q-item-label>Não há nenhum quadro criado</q-item-label>
              <q-item-label caption>Clique aqui para criar um</q-item-label>
            </q-item-section>
          </q-item>
        </q-expansion-item>
        <q-expansion-item label="Configurações">
          <q-item
            clickable
            @click="openModalNovoQuadro()"
            v-show="this.projects.length === 0"
          >
            <q-item-section>
              <q-item-label>Gestão de usuários</q-item-label>
              <q-item-label caption>Criar ou excluir usuários</q-item-label>
            </q-item-section>
          </q-item>
        </q-expansion-item>
        <q-item clickable @click="logout()">
          <q-item-section>
            <q-item-label>Sair</q-item-label>
          </q-item-section>
        </q-item>
      </q-list>
    </q-drawer>

    <q-page-container>
      <q-tab-panels v-model="tab" animated v-show="actualQuadroId !== null">
        <q-tab-panel name="CREATED">
          <task-list
            v-show="actualQuadroId !== null"
            :actual-status="this.tab"
            :quadro-id="this.actualQuadroId"
            :tasks="this.createdTasks"
            @update-task="submitTaskForm($event, 'updated')"
            @delete-task="onTaskDelete()"
            @delete-project="onProjectDelete()"
            @open-new-task-modal="openNewTaskModal()"
          ></task-list>
        </q-tab-panel>
        <q-tab-panel name="IN_PROGRESS">
          <task-list
            v-show="actualQuadroId !== null"
            :actual-status="this.tab"
            :quadro-id="this.actualQuadroId"
            :tasks="this.inProgressTasks"
            @update-task="submitTaskForm($event, 'updated')"
            @delete-task="onTaskDelete()"
            @delete-project="onProjectDelete()"
            @open-new-task-modal="openNewTaskModal()"
          ></task-list>
        </q-tab-panel>
        <q-tab-panel name="DONE">
          <task-list
            v-show="actualQuadroId !== null"
            :actual-status="this.tab"
            :quadro-id="this.actualQuadroId"
            :tasks="this.doneTasks"
            @update-task="submitTaskForm($event, 'updated')"
            @delete-task="onTaskDelete()"
            @delete-project="onProjectDelete()"
            @open-new-task-modal="openNewTaskModal()"
          ></task-list>
        </q-tab-panel>
        <q-tab-panel name="CANCELED">
          <task-list
            v-show="actualQuadroId !== null"
            :actual-status="this.tab"
            :quadro-id="this.actualQuadroId"
            :tasks="this.canceledTasks"
            @update-task="submitTaskForm($event, 'updated')"
            @delete-task="onTaskDelete()"
            @delete-project="onProjectDelete()"
            @open-new-task-modal="openNewTaskModal()"
          />
        </q-tab-panel>
      </q-tab-panels>

      <router-view
        v-show="this.actualQuadroId === null"
        @open-modal-novo-quadro="openModalNovoQuadro()"
        @open-modal-novo-usuario="openModalNovoUsuario()"
      />

      <new-task-modal
        :projects-list="this.projectsList"
        v-model="taskModalOpen"
        @submit-task-form="submitTaskForm($event, 'created')"
      />
      <modal-novo-usuario
        v-model="modalNovoUsuarioOpen"
        @update:modalNovoUsuarioOpen="modalNovoUsuarioOpen = $event"
        @hide-modal="modalNovoUsuarioOpen = false"
        @submit-form-novo-usuario="submitFormNovoUsuario()"
      />
      <modal-novo-quadro
        :lista-usuarios="this.listaUsuarios"
        v-model="modalNovoQuadroOpen"
        @submit-form-novo-quadro="submitFormNovoQuadro($event)"
      />
    </q-page-container>
  </q-layout>
</template>

<script>
import { defineComponent, ref } from "vue";
import { useStore } from "vuex";
import HeaderComponent from "src/components/HeaderComponent.vue";
import NewTaskModal from "src/components/NewTaskModal.vue";
import ModalNovoUsuario from "src/components/ModalNovoUsuario.vue";
import ModalNovoQuadro from "src/components/ModalNovoQuadro.vue";
import ProjectsList from "src/components/ProjectsList.vue";
import TaskList from "src/pages/TaskList.vue";
import UsuarioService from "src/services/UsuarioService";
import QuadroService from "src/services/QuadroService";
import ProjectService from "src/services/ProjectService";
import TaskService from "src/services/TaskService";
import CustomerService from "src/services/CustomerService";

export default defineComponent({
  name: "MainLayout",

  setup() {
    const store = useStore();

    return {
      store,
    };
  },

  components: {
    HeaderComponent,
    NewTaskModal,
    ModalNovoUsuario,
    ModalNovoQuadro,
    ProjectsList,
    TaskList,
  },

  data() {
    return {
      tab: "CREATED",
      projects: [],
      tasks: [],
      projectsList: [],
      customers: [],
      customersList: [],
      usuarios: [],
      listaUsuarios: [],
      quadros: [],
      createdTasks: [],
      inProgressTasks: [],
      doneTasks: [],
      canceledTasks: [],
      actualQuadroId: null,
      leftDrawerOpen: ref(false),
      taskModalOpen: ref(false),
      modalNovoUsuarioOpen: ref(false),
      modalNovoQuadroOpen: ref(false),
    };
  },

  methods: {
    fetch() {
      this.getUsuarioInfo();
      this.getProjects();
      this.getCustomers();
    },
    setQuadroId(quadroId) {
      this.actualQuadroId = quadroId;
    },
    async getProjects() {
      this.projects = (await ProjectService.listProjects()).data;
      this.projectsList = this.projects.map((project) => {
        return {
          label: project.projectName,
          value: project.id,
        };
      });
    },
    async getUsuarioInfo() {
      this.listaUsuarios = [];
      this.usuarios = (await UsuarioService.getUsuarioInfo()).data;
      console.log(this.usuarios);

      this.usuarios.forEach((usuario) => {
        this.quadros.push(usuario.quadros);
        this.listaUsuarios.push({
          label: usuario.nome,
          value: usuario.id,
        });
      });

      console.log(this.usuarios);
    },
    async getTasksByProjectId(projectId) {
      if (projectId === null) return;
      this.clearTaskLists();
      this.actualProjectId = projectId;
      this.tasks = (await TaskService.listTasksByProject(projectId)).data;
      this.tasks.forEach((task) => {
        if (task.taskStatus === "CREATED") {
          this.createdTasks.push(task);
        } else if (task.taskStatus === "IN_PROGRESS") {
          this.inProgressTasks.push(task);
        } else if (task.taskStatus === "DONE") {
          this.doneTasks.push(task);
        } else if (task.taskStatus === "CANCELED") {
          this.canceledTasks.push(task);
        }
      });
    },
    submitFormNovoQuadro(quadro) {
      QuadroService.salvarQuadro(quadro).then(() => {
        this.fetch();
        this.modalNovoQuadroOpen = false;
      });
      this.returnFeedbackMessage("Quadro criado com sucesso!");
    },
    submitFormNovoUsuario() {
      this.fetch();
      this.modalNovoUsuarioOpen = false;
      this.returnFeedbackMessage("Usuário criado com sucesso!");
    },
    submitTaskForm(task, action) {
      TaskService.saveTask(task).then(() => {
        this.fetch();
        this.taskModalOpen = false;
      });
      this.returnFeedbackMessage("Task " + action + " successfully!");
    },
    onTaskDelete() {
      this.returnFeedbackMessage("Task deleted successfully!");
      this.fetch();
    },
    onProjectDelete() {
      this.returnFeedbackMessage("Project deleted successfully!");
      this.actualProjectId = null;
      this.fetch();
    },
    openNewTaskModal() {
      this.taskModalOpen = true;
    },
    openModalNovoUsuario() {
      this.modalNovoUsuarioOpen = true;
    },
    openModalNovoQuadro() {
      this.modalNovoQuadroOpen = true;
    },
    toggleLeftDrawer() {
      this.leftDrawerOpen = !this.leftDrawerOpen;
    },
    returnFeedbackMessage(notifyMessage) {
      this.$q.notify({
        type: "positive",
        color: "teal",
        message: notifyMessage,
        position: "bottom-right",
      });
    },
    clearTaskLists() {
      this.tasks = [];
      this.createdTasks = [];
      this.inProgressTasks = [];
      this.doneTasks = [];
      this.canceledTasks = [];
    },
    getTaskListSize(tasks) {
      return tasks.length;
    },
    getOpenTasksSize(tasks) {
      return tasks.filter(
        (task) =>
          task.taskStatus === "CREATED" || task.taskStatus === "IN_PROGRESS"
      ).length;
    },
    logout() {
      console.log("Logout");
      this.$store.dispatch("logout");
      this.$router.push({ path: "/auth/login" });
    },
  },

  mounted() {
    this.fetch();
  },
});
</script>
