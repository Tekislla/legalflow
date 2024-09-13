<template>
  <q-dialog>
    <q-card class="q-pa-md main-card">
      <q-card-section class="row items-center q-pb-none">
        <div class="text-h6">Novo Processo</div>
        <q-space />
      </q-card-section>

      <q-card-section>
        <div class="q-gutter-md">
          <q-input
            v-model="novoProcesso.nome"
            label="Nome"
            outlined
            required
            dense
            :rules="[
              (val) =>
                val.length <= 100 || 'Por favor, use no máximo 100 caracteres',
            ]"
          />

          <q-input
            v-model="novoProcesso.numero"
            label="Número do processo"
            outlined
            required
            dense
            :rules="[
              (val) =>
                val.length <= 100 || 'Por favor, use no máximo 100 caracteres',
            ]"
          />

          <q-input
            v-model="novoProcesso.descricao"
            label="Descrição"
            outlined
            required
            dense
            type="textarea"
            hint="Máximo 1000 caracteres"
            :rules="[
              (val) =>
                val.length <= 1000 ||
                'Por favor, use no máximo 1000 caracteres',
            ]"
          />

          <div class="row">
            <div class="col date-input-left">
              <q-input
                v-model="novoProcesso.prazoSubsidio"
                label="Prazo de subsídio"
                outlined
                required
                dense
                :mask="dateMask"
                type="date"
              >
                <template v-slot:append>
                  <q-icon name="event" />
                </template>
              </q-input>
            </div>

            <div class="col date-input-right">
              <q-input
                v-model="novoProcesso.prazoFatal"
                label="Prazo fatal"
                outlined
                required
                dense
                :mask="dateMask"
                type="date"
              >
                <template v-slot:append>
                  <q-icon name="event" />
                </template>
              </q-input>
            </div>
          </div>

          <q-input
            v-model="novoProcesso.arquivo"
            label="Arquivo"
            outlined
            required
            dense
            type="file"
          />

          <q-card-actions align="right">
            <q-btn flat label="Cancel" color="black" no-caps v-close-popup />
            <q-btn
              @click="submitTaskForm()"
              unelevated
              size="md"
              label="Create task"
              no-caps
              color="teal"
              :disable="
                !newTaskName ||
                !newTaskDescription ||
                !selectedProject ||
                newTaskDescription.length > 1000 ||
                newTaskName.length > 100
              "
            />
          </q-card-actions>
        </div>
      </q-card-section>
    </q-card>
  </q-dialog>
</template>
<script>
import { defineComponent } from "vue";

export default defineComponent({
  name: "ModalNovoProcesso",

  props: {
    actualQuadroId: {
      type: Number,
    },
  },

  data() {
    return {
      novoProcesso: {
        nome: "",
        numero: "",
        descricao: "",
        prazoSubsidio: "",
        prazoFatal: "",
        arquivo: null,
        quadroId: this.actualQuadroId,
      },
      dateMask: "DD-MM-YYYY",
    };
  },

  methods: {
    submitTaskForm() {
      this.$emit("submit-task-form", {
        taskName: this.newTaskName,
        taskDescription: this.newTaskDescription,
        taskStatus: "CREATED",
        projectId: this.selectedProject.value,
      });

      this.clearForm();
    },
    clearForm() {
      this.newTaskName = "";
      this.newTaskDescription = "";
      this.selectedProject = null;
    },
  },
});
</script>
