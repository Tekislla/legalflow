<template>
  <q-dialog>
    <q-card class="q-pa-md main-card">
      <q-card-section class="row items-center q-pb-none">
        <div class="text-h6">New Customer</div>
        <q-space />
      </q-card-section>

      <q-card-section>
        <div class="q-gutter-md">
          <q-input
            v-model="newCustomerName"
            label="Name"
            outlined
            required
            dense
            :rules="[
              (val) => val.length <= 100 || 'Please use maximum 100 characters',
            ]"
          />

          <q-card-actions align="right">
            <q-btn flat label="Cancel" color="black" no-caps v-close-popup />
            <q-btn
              @click="submitCustomerForm()"
              unelevated
              size="md"
              label="Create customer"
              no-caps
              color="teal"
              :disable="!newCustomerName || newCustomerName.length > 100"
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
  name: "NewCustomerModal",

  data() {
    return {
      newCustomerName: "",
    };
  },

  methods: {
    submitCustomerForm() {
      this.$emit("submit-customer-form", {
        customerName: this.newCustomerName,
      });

      this.clearForm();
    },
    clearForm() {
      this.newCustomerName = "";
    },
  },
});
</script>
