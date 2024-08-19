<template>
  <q-layout view="hHh lpR fFf">
    <header-component
      :created-tasks="getTaskListSize(createdTasks)"
      :in-progress-tasks="getTaskListSize(inProgressTasks)"
      :done-tasks="getTaskListSize(doneTasks)"
      :canceled-tasks="getTaskListSize(canceledTasks)"
      :actual-project-id="actualProjectId"
      @toggle-left-drawer="toggleLeftDrawer()"
      @update:tab="tab = $event"
      @open-new-customer-modal="openNewCustomerModal()"
      @open-new-project-modal="openNewProjectModal()"
      @update:projectModalOpen="projectModalOpen = $event"
    />

    <q-drawer v-model="leftDrawerOpen" show-if-above bordered>
      <q-list>
        <q-item clickable @click="this.actualProjectId = null">
          <q-item-section avatar> Home </q-item-section>
        </q-item>
        <q-expansion-item label="Projects">
          <projects-list
            v-for="project in projects"
            :key="project.id"
            v-bind:project="project"
            :projects="projects"
            :actual-project-id="actualProjectId"
            :task-list-size="getOpenTasksSize(project.tasks)"
            @get-tasks-by-project-id="getTasksByProjectId(project.id)"
          />
          <q-item
            clickable
            @click="openNewProjectModal()"
            v-show="this.projects.length === 0"
          >
            <q-item-section>
              <q-item-label>There are no projects created.</q-item-label>
              <q-item-label caption>Click here to create one</q-item-label>
            </q-item-section>
          </q-item>
        </q-expansion-item>
      </q-list>
    </q-drawer>

    <q-page-container>
      <q-tab-panels v-model="tab" animated v-show="actualProjectId !== null">
        <q-tab-panel name="CREATED">
          <task-list
            v-show="actualProjectId !== null"
            :actual-status="this.tab"
            :project-id="this.actualProjectId"
            :tasks="this.createdTasks"
            @update-task="submitTaskForm($event, 'updated')"
            @delete-task="onTaskDelete()"
            @delete-project="onProjectDelete()"
            @open-new-task-modal="openNewTaskModal()"
          ></task-list>
        </q-tab-panel>
        <q-tab-panel name="IN_PROGRESS">
          <task-list
            v-show="actualProjectId !== null"
            :actual-status="this.tab"
            :project-id="this.actualProjectId"
            :tasks="this.inProgressTasks"
            @update-task="submitTaskForm($event, 'updated')"
            @delete-task="onTaskDelete()"
            @delete-project="onProjectDelete()"
            @open-new-task-modal="openNewTaskModal()"
          ></task-list>
        </q-tab-panel>
        <q-tab-panel name="DONE">
          <task-list
            v-show="actualProjectId !== null"
            :actual-status="this.tab"
            :project-id="this.actualProjectId"
            :tasks="this.doneTasks"
            @update-task="submitTaskForm($event, 'updated')"
            @delete-task="onTaskDelete()"
            @delete-project="onProjectDelete()"
            @open-new-task-modal="openNewTaskModal()"
          ></task-list>
        </q-tab-panel>
        <q-tab-panel name="CANCELED">
          <task-list
            v-show="actualProjectId !== null"
            :actual-status="this.tab"
            :project-id="this.actualProjectId"
            :tasks="this.canceledTasks"
            @update-task="submitTaskForm($event, 'updated')"
            @delete-task="onTaskDelete()"
            @delete-project="onProjectDelete()"
            @open-new-task-modal="openNewTaskModal()"
          />
        </q-tab-panel>
      </q-tab-panels>

      <router-view
        v-show="this.actualProjectId === null"
        @open-new-project-modal="openNewProjectModal()"
        @open-new-customer-modal="openNewCustomerModal()"
      />

      <new-task-modal
        :projects-list="this.projectsList"
        v-model="taskModalOpen"
        @submit-task-form="submitTaskForm($event, 'created')"
      />
      <new-customer-modal
        v-model="customerModalOpen"
        @update:customerModalOpen="customerModalOpen = $event"
        @hide-modal="customerModalOpen = false"
        @submit-customer-form="submitCustomerForm($event)"
      />
      <new-project-modal
        :customer-list="this.customersList"
        v-model="projectModalOpen"
        @submit-project-form="submitProjectForm($event)"
      />
    </q-page-container>
  </q-layout>
</template>

<script>
import { defineComponent, ref } from "vue";
import HeaderComponent from "src/components/HeaderComponent.vue";
import NewTaskModal from "src/components/NewTaskModal.vue";
import NewCustomerModal from "src/components/NewCustomerModal.vue";
import NewProjectModal from "src/components/NewProjectModal.vue";
import ProjectsList from "src/components/ProjectsList.vue";
import TaskList from "src/pages/TaskList.vue";
import ProjectService from "src/services/ProjectService";
import TaskService from "src/services/TaskService";
import CustomerService from "src/services/CustomerService";

export default defineComponent({
  name: "MainLayout",

  components: {
    HeaderComponent,
    NewTaskModal,
    NewCustomerModal,
    NewProjectModal,
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
      createdTasks: [],
      inProgressTasks: [],
      doneTasks: [],
      canceledTasks: [],
      actualProjectId: null,
      leftDrawerOpen: ref(false),
      taskModalOpen: ref(false),
      customerModalOpen: ref(false),
      projectModalOpen: ref(false),
    };
  },

  methods: {
    fetch() {
      this.getProjects();
      this.getCustomers();
      this.getTasksByProjectId(this.actualProjectId);
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
    async getCustomers() {
      this.customers = (await CustomerService.listCustomers()).data;
      this.customersList = this.customers.map((customer) => {
        return {
          label: customer.customerName,
          value: customer.id,
        };
      });
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
    submitProjectForm(project) {
      ProjectService.saveProject(project).then(() => {
        this.fetch();
        this.projectModalOpen = false;
      });
      this.returnFeedbackMessage("Project created successfully!");
    },
    submitCustomerForm(customer) {
      CustomerService.saveCustomer(customer).then(() => {
        this.getCustomers();
        this.customerModalOpen = false;
      });
      this.returnFeedbackMessage("Customer created successfully!");
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
    openNewCustomerModal() {
      this.customerModalOpen = true;
    },
    openNewProjectModal() {
      this.projectModalOpen = true;
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
  },

  mounted() {
    this.fetch();
  },
});
</script>
