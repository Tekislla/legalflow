<template>
  <q-dialog>
    <q-card class="q-pa-md main-card">
      <q-card-section class="row items-center q-pb-none">
        <div class="text-h6">Novo Quadro</div>
        <q-space />
      </q-card-section>

      <q-card-section>
        <div class="q-gutter-md">
          <q-input
            v-model="form.nome"
            label="Nome"
            outlined
            required
            dense
            :rules="[
              (val) => val.length <= 50 || 'Please use maximum 50 characters',
            ]"
          />

          <q-select
            v-model="usuarioSelecionado"
            :options="listaUsuarios"
            label="Usuário"
            outlined
            required
            dense
          />

          <q-card-actions align="right">
            <q-btn flat label="Cancel" color="black" no-caps v-close-popup />
            <q-btn
              @click="salvarQuadro()"
              unelevated
              size="md"
              label="Criar quadro"
              no-caps
              color="teal"
              :disable="
                !form.nome || !usuarioSelecionado || form.nome.length > 50
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
  name: "ModalNovoQuadro",

  props: {
    listaUsuarios: {
      type: Array,
      required: true,
    },
  },

  data() {
    return {
      form: {
        nome: "",
        usuarioId: null,
      },
      usuarioSelecionado: null,
    };
  },

  methods: {
    salvarQuadro() {
      this.form.usuarioId = this.usuarioSelecionado.value;
      this.$emit("salvar-quadro", this.form);
      console.log(this.form);
    },
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
