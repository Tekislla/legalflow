<template>
  <q-page class="main-page flex">
    <div class="container text-center q-pa-md flex flex-center">
      <q-card class="container-card q-pa-lg">
        <q-card-section class="btn-card q-gutter-md">
          <q-btn
            v-show="userRole === 'ADMIN'"
            v-on:click="this.$emit('open-modal-novo-processo')"
            unelevated
            size="md"
            label="Novo Processo"
            no-caps
            color="teal"
            :disable="quadroId === null"
          />
          <q-btn
            v-show="userRole === 'ADMIN'"
            v-on:click="confirmaDeletarQuadro(quadroId)"
            unelevated
            size="md"
            label="Deletar Quadro"
            no-caps
            color="black"
            :disable="quadroId === null"
          />
        </q-card-section>

        <q-card-section>
          <q-table
            :rows="tasks"
            :columns="columns"
            row-key="id"
            no-data-label="Não há processos disponíveis com este status."
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
import { useStore } from "vuex";
import TaskService from "src/services/TaskService";
import ProjectService from "src/services/ProjectService";
import QuadroService from "src/services/QuadroService";

export default defineComponent({
  name: "ListaProcessos",

  props: {
    actualStatus: String,
    tasks: Array,
    quadroId: Number,
  },

  setup() {
    const store = useStore();
    const userRole = store.state.usuario.role;

    return {
      store,
      userRole,
    };
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
          label: "Criado",
          value: "CRIADO",
        },
        {
          label: "Em Progresso",
          value: "EM_PROGRESSO",
        },
        {
          label: "Finalizado",
          value: "FINALIZADO",
        },
        {
          label: "Arquivado",
          value: "ARQUIVADO",
        },
      ],
      columns: [
        {
          name: "details",
          label: "Detalhes  ",
          align: "left",
        },
        {
          name: "numero",
          required: true,
          label: "Número",
          align: "left",
          field: (row) => row.numero,
          format: (val) => `${val}`,
          sortable: true,
        },
        {
          name: "nome",
          required: true,
          label: "Nome",
          align: "left",
          field: (row) => row.nome,
          format: (val) => `${this.formatTextSize(val, 30)}`,
          sortable: true,
        },
        {
          name: "responsavel",
          required: true,
          label: "Responsável",
          align: "left",
          field: (row) => row.responsavel,
          format: (val) => `${val}`,
          sortable: true,
        },
        {
          name: "prazoSubsidio",
          required: true,
          label: "Prazo Subsídio",
          align: "left",
          field: (row) => row.prazoSubsidio,
          format: (val) => `${this.formatTextSize(val, 30)}`,
          sortable: true,
        },
        {
          name: "prazoFatal",
          required: true,
          label: "Prazo Fatal",
          align: "left",
          field: (row) => row.prazoFatal,
          format: (val) => `${this.formatTextSize(val, 30)}`,
          sortable: true,
        },
        {
          name: "actions",
          label: "Ações",
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
    confirmaDeletarQuadro(quadroId) {
      let confirmationMessage = "Tem certeza que deseja excluir este quadro?";

      if (this.tasks.length > 0)
        confirmationMessage =
          "Este quadro possui processos associados à ele. Tem certeza que deseja excluir este quadro?";

      this.$q
        .dialog({
          title: "Deletar quadro",
          message: confirmationMessage,
          persistent: true,
          ok: {
            label: "Sim",
            color: "black",
            noCaps: true,
          },
          cancel: {
            label: "Não",
            color: "black",
            noCaps: true,
            flat: true,
          },
        })
        .onOk(() => {
          this.deletarQuadro(quadroId);
        });
    },
    deleteTask(task) {
      TaskService.deleteTask(task.id).then(() => {
        this.$emit("delete-task");
        this.isEditingTask = false;
        this.taskDetailsModalOpen = false;
      });
    },
    deletarQuadro(quadroId) {
      QuadroService.deletarQuadro(quadroId).then(() => {
        this.$emit("deletar-quadro");
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
