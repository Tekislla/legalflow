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
            stack-label
            label="Nome"
            outlined
            required
            :rules="[
              (val) => val.length <= 20 || 'Máximo de 50 caracteres',
              (val) => val.length >= 5 || 'Mínimo de 5 caracteres',
            ]"
          />

          <q-select
            v-model="usuarioSelecionado"
            stack-label
            :options="listaUsuarios"
            label="Usuário"
            outlined
            required
          />

          <q-card-actions align="right">
            <q-btn
              flat
              @click="clearForm()"
              label="Cancel"
              color="black"
              no-caps
              v-close-popup
            />
            <q-btn
              :loading="loading"
              @click="salvarQuadro()"
              unelevated
              size="md"
              label="Criar quadro"
              no-caps
              color="teal"
              :disable="
                loading ||
                !form.nome ||
                !usuarioSelecionado ||
                form.nome.length < 5 ||
                form.nome.length > 50
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
import QuadroService from "@/services/QuadroService";

export default defineComponent({
  name: "ModalNovoQuadro",

  props: {
    listaUsuarios: Array,
  },

  data() {
    return {
      loading: false,
      form: {
        nome: "",
        usuarioId: null,
      },
      usuarioSelecionado: null,
    };
  },

  methods: {
    async salvarQuadro() {
      this.loading = true;
      this.form.usuarioId = this.usuarioSelecionado.value;

      await QuadroService.salvarQuadro(this.form)
        .then(() => {
          this.loading = false;
          this.$emit("salvar-quadro");
          this.clearForm();
        })
        .catch((err) => {
          this.loading = false;
          NotificationUtil.returnFeedbackMessage(
            this.$q,
            err.response?.data || "Falha ao criar quadro",
            "negative",
            "red"
          );
        });
    },
    clearForm() {
      this.form = {
        nome: "",
        usuarioId: null,
      };
      this.usuarioSelecionado = null;
    },
  },
});
</script>
