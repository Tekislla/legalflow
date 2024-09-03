<template>
  <q-dialog>
    <q-card class="q-pa-md main-card">
      <q-card-section class="row items-center q-pb-none">
        <div class="text-h6">New Task</div>
        <q-space />
      </q-card-section>

      <q-card-section>
        <div class="q-gutter-md">
          <q-input
            v-model="newTaskName"
            label="Name"
            outlined
            required
            dense
            :rules="[
              (val) => val.length <= 100 || 'Please use maximum 100 characters',
            ]"
          />

          <q-input
            v-model="newTaskDescription"
            label="Description"
            outlined
            required
            dense
            type="textarea"
            hint="Max 1000 characters"
            :rules="[
              (val) =>
                val.length <= 1000 || 'Please use maximum 1000 characters',
            ]"
          />

          <q-select
            v-model="selectedProject"
            :options="projectsList"
            label="Project"
            outlined
            required
            dense
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
    quadrosList: {
      type: Array,
      required: true,
    },
  },

  data() {
    return {
      newTaskName: "",
      newTaskDescription: "",
      selectedProject: null,
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
