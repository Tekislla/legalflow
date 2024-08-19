<template>
  <q-page class="main-page flex">
    <div class="container text-center q-pa-md flex flex-center">
      <q-card class="container-card q-pa-lg">
        <q-card-section class="btn-card q-gutter-md">
          <q-btn
            v-on:click="this.$emit('open-new-task-modal')"
            unelevated
            size="md"
            label="New Task"
            no-caps
            color="teal"
            :disable="projectId === null"
          />
          <q-btn
            v-on:click="confirmDeleteProject(projectId)"
            unelevated
            size="md"
            label="Delete Project"
            no-caps
            color="black"
            :disable="projectId === null"
          />
        </q-card-section>

        <q-card-section>
          <q-table
            :rows="tasks"
            :columns="columns"
            row-key="id"
            no-data-label="There are no tasks available"
            v-model:pagination="pagination"
            :rows-per-page-options="[10, 25, 50, 100, 0]"
          >
            <template v-slot:body-cell-details="props">
              <q-td :props="props">
                <q-btn-group unelevated>
                  <q-btn
                    size="md"
                    round
                    icon="open_in_new"
                    @click="openTaskDetailsModal(props.row)"
                  >
                  </q-btn>
                </q-btn-group>
              </q-td>
            </template>
            <template v-slot:body-cell-actions="props">
              <q-td :props="props">
                <q-btn-group unelevated>
                  <q-btn
                    size="md"
                    round
                    icon="add_task"
                    @click="changeStatus(props.row, 'CREATED')"
                    :disabled="this.actualStatus === 'CREATED'"
                  >
                    <q-tooltip>Mark as created</q-tooltip>
                  </q-btn>
                  <q-btn
                    size="md"
                    round
                    icon="construction"
                    @click="changeStatus(props.row, 'IN_PROGRESS')"
                    :disabled="this.actualStatus === 'IN_PROGRESS'"
                  >
                    <q-tooltip>Mark as in progress</q-tooltip>
                  </q-btn>
                  <q-btn
                    size="md"
                    round
                    icon="done_all"
                    @click="changeStatus(props.row, 'DONE')"
                    :disabled="this.actualStatus === 'DONE'"
                  >
                    <q-tooltip>Mark as done</q-tooltip>
                  </q-btn>
                  <q-btn
                    size="md"
                    round
                    icon="block"
                    @click="changeStatus(props.row, 'CANCELED')"
                    :disabled="this.actualStatus === 'CANCELED'"
                  >
                    <q-tooltip>Cancel task</q-tooltip>
                  </q-btn>
                </q-btn-group>
              </q-td>
            </template>
          </q-table>
        </q-card-section>
      </q-card>

      <q-dialog persistent v-model="taskDetailsModalOpen">
        <q-card class="q-pa-md main-card">
          <q-card-section class="row items-center q-pb-none">
            <div class="col-6">
              <div class="text-h6">
                {{ this.formatTextSize(selectedTask.taskName, 15) }}
              </div>
            </div>
            <div class="col-6 text-right">
              <q-select
                v-model="selectedTaskStatus"
                :options="taskStatusList"
                label="Status"
                outlined
                required
                dense
                :disable="!isEditingTask"
              />
            </div>
          </q-card-section>

          <q-card-section>
            <div class="q-gutter-md">
              <q-input
                v-show="isEditingTask"
                v-model="selectedTaskName"
                label="Name"
                outlined
                required
                dense
                :rules="[
                  (val) =>
                    val.length <= 100 || 'Please use maximum 100 characters',
                ]"
              />

              <q-input
                v-model="selectedTaskDescription"
                label="Description"
                outlined
                required
                dense
                :disable="!isEditingTask"
                type="textarea"
                :hint="isEditingTask ? 'Max 1000 characters' : ''"
                :rules="[
                  (val) =>
                    val.length <= 1000 || 'Please use maximum 1000 characters',
                ]"
              />

              <q-card-actions>
                <div class="flex-container">
                  <div class="left-buttons">
                    <q-btn
                      unelevated
                      @click="confirmDeleteTask(selectedTask)"
                      label="Delete task"
                      color="black"
                      no-caps
                    />
                  </div>
                  <div class="right-buttons">
                    <q-btn
                      v-show="!isEditingTask"
                      flat
                      @click="isEditingTask = false"
                      label="Close"
                      color="black"
                      no-caps
                      v-close-popup
                    />
                    <q-btn
                      v-show="isEditingTask"
                      flat
                      @click="isEditingTask = false"
                      label="Cancel"
                      color="black"
                      no-caps
                    />
                    <q-btn
                      v-show="!isEditingTask"
                      unelevated
                      @click="isEditingTask = true"
                      label="Edit task"
                      color="teal"
                      no-caps
                    />
                    <q-btn
                      v-show="isEditingTask"
                      unelevated
                      @click="saveTask(selectedTask)"
                      label="Save task"
                      color="teal"
                      no-caps
                      :disable="
                        !selectedTaskName ||
                        !selectedTaskDescription ||
                        selectedTaskName.length > 100 ||
                        selectedTaskDescription.length > 1000 ||
                        selectedTaskStatus === ''
                      "
                    />
                  </div>
                </div>
              </q-card-actions>
            </div>
          </q-card-section>
        </q-card>
      </q-dialog>
    </div>
  </q-page>
</template>

<script>
import { defineComponent } from "vue";
import TaskService from "src/services/TaskService";
import ProjectService from "src/services/ProjectService";

export default defineComponent({
  name: "TaskList",

  props: {
    actualStatus: String,
    tasks: Array,
    projectId: Number,
  },

  data() {
    return {
      taskDetailsModalOpen: false,
      isEditingTask: false,
      selectedTask: {},
      selectedTaskName: "",
      selectedTaskDescription: "",
      selectedTaskStatus: "",
      pagination: {
        rowsPerPage: 10,
      },
      taskStatusList: [
        {
          label: "Created",
          value: "CREATED",
        },
        {
          label: "In Progress",
          value: "IN_PROGRESS",
        },
        {
          label: "Done",
          value: "DONE",
        },
        {
          label: "Canceled",
          value: "CANCELED",
        },
      ],
      columns: [
        {
          name: "details",
          label: "Details",
          align: "left",
        },
        {
          name: "taskName",
          required: true,
          label: "Task Name",
          align: "left",
          field: (row) => row.taskName,
          format: (val) => `${this.formatTextSize(val, 30)}`,
          sortable: true,
        },
        {
          name: "taskDescription",
          required: true,
          label: "Description",
          align: "left",
          field: (row) => row.taskDescription,
          format: (val) => `${this.formatTextSize(val, 30)}`,
          sortable: true,
        },
        {
          name: "taskStatus",
          required: true,
          label: "Status",
          align: "left",
          field: (row) => row.taskStatus,
          format: (val) => `${this.formatTaskStatus(val)}`,
          sortable: true,
        },
        {
          name: "customerName",
          required: true,
          label: "Customer",
          align: "left",
          field: (row) => row.customerName,
          format: (val) => `${val}`,
          sortable: true,
        },
        {
          name: "projectName",
          required: true,
          label: "Project",
          align: "left",
          field: (row) => row.projectName,
          format: (val) => `${val}`,
          sortable: true,
        },
        {
          name: "actions",
          label: "Quick actions",
          align: "right",
        },
      ],
    };
  },

  methods: {
    changeStatus(task, status) {
      this.$emit("update-task", {
        id: task.id,
        taskName: task.taskName,
        taskDescription: task.taskDescription,
        taskStatus: status,
        projectId: this.projectId,
      });
    },
    saveTask(task) {
      this.$emit("update-task", {
        id: task.id,
        taskName: this.selectedTaskName,
        taskDescription: this.selectedTaskDescription,
        taskStatus: this.selectedTaskStatus.value,
        projectId: this.projectId,
      });
      this.isEditingTask = false;
      this.taskDetailsModalOpen = false;
    },
    confirmDeleteTask(task) {
      this.$q
        .dialog({
          title: "Delete task",
          message: "Are you sure you want to delete this task?",
          persistent: true,
          ok: {
            label: "Yes",
            color: "black",
            noCaps: true,
          },
          cancel: {
            label: "No",
            color: "black",
            noCaps: true,
            flat: true,
          },
        })
        .onOk(() => {
          this.deleteTask(task);
        });
    },
    confirmDeleteProject(projectId) {
      let confirmationMessage = "Are you sure you want to delete this project?";

      if (this.tasks.length > 0)
        confirmationMessage =
          "This project has tasks. Are you sure you want to delete this project?";

      this.$q
        .dialog({
          title: "Delete project",
          message: confirmationMessage,
          persistent: true,
          ok: {
            label: "Yes",
            color: "black",
            noCaps: true,
          },
          cancel: {
            label: "No",
            color: "black",
            noCaps: true,
            flat: true,
          },
        })
        .onOk(() => {
          this.deleteProject(projectId);
        });
    },
    deleteTask(task) {
      TaskService.deleteTask(task.id).then(() => {
        this.$emit("delete-task");
        this.isEditingTask = false;
        this.taskDetailsModalOpen = false;
      });
    },
    deleteProject(projectId) {
      ProjectService.deleteProject(projectId).then(() => {
        this.$emit("delete-project");
      });
    },
    openTaskDetailsModal(task) {
      this.selectedTask = task;
      this.selectedTaskName = task.taskName;
      this.selectedTaskStatus = this.taskStatusList.find(
        (status) => status.value === task.taskStatus
      );
      this.selectedTaskDescription = task.taskDescription;
      this.taskDetailsModalOpen = true;
    },
    formatTaskStatus(taskStatus) {
      return this.taskStatusList.find((status) => status.value === taskStatus)
        .label;
    },
    formatTextSize(text, limit) {
      if (!text) return "";
      return text.length > limit ? text.substring(0, limit) + "..." : text;
    },
  },
});
</script>
