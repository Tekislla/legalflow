<template>
  <q-dialog>
    <q-card class="q-pa-md main-card">
      <q-card-section class="row items-center q-pb-none">
        <div class="text-h6">New Project</div>
        <q-space />
      </q-card-section>

      <q-card-section>
        <div class="q-gutter-md">
          <q-input
            v-model="newProjectName"
            label="Name"
            outlined
            required
            dense
            :rules="[
              (val) => val.length <= 50 || 'Please use maximum 50 characters',
            ]"
          />

          <q-select
            v-model="selectedCustomer"
            :options="customerList"
            label="Customer"
            outlined
            required
            dense
          />

          <q-card-actions align="right">
            <q-btn flat label="Cancel" color="black" no-caps v-close-popup />
            <q-btn
              @click="submitProjectForm()"
              unelevated
              size="md"
              label="Create project"
              no-caps
              color="teal"
              :disable="
                !newProjectName ||
                !selectedCustomer ||
                newProjectName.length > 50
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
  name: "NewProjectModal",

  props: {
    customerList: {
      type: Array,
      required: true,
    },
  },

  data() {
    return {
      newProjectName: "",
      selectedCustomer: null,
    };
  },

  methods: {
    submitProjectForm() {
      this.$emit("submit-project-form", {
        projectName: this.newProjectName,
        customerId: this.selectedCustomer.value,
      });

      this.clearForm();
    },
    clearForm() {
      this.newProjectName = "";
      this.selectedCustomer = null;
    },
  },
});
</script>
