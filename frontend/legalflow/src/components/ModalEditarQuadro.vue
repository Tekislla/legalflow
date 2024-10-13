<template>
  <q-dialog>
    <q-card class="q-pa-md main-card">
      <q-card-section class="row items-center q-pb-none">
        <div class="text-h6">Editar Quadro</div>
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
            <q-btn flat label="Cancel" color="black" no-caps v-close-popup />
            <q-btn
              @click="salvarQuadro()"
              unelevated
              size="md"
              label="Salvar quadro"
              no-caps
              color="teal"
              :disable="
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
import NotificationUtil from "@/utils/NotificationUtil";

export default defineComponent({
  name: "ModalEditarQuadro",

  props: {
    idQuadroAtual: Number,
    listaUsuarios: Array,
    nomeQuadro: String,
    usuarioResponsavelQuadro: Object,
  },

  data() {
    return {
      form: {
        id: this.idQuadroAtual,
        nome: this.nomeQuadro,
        usuarioId: null,
      },
      usuarioSelecionado: this.usuarioResponsavelQuadro,
    };
  },

  watch: {
    nomeQuadro(newValue) {
      this.form.nome = newValue;
    },
    usuarioResponsavelQuadro(newValue) {
      this.usuarioSelecionado = newValue;
    },
    idQuadroAtual(newValue) {
      this.form.id = newValue;
    },
    modalOpen(newValue) {
      if (newValue) {
        this.form.nome = this.nomeQuadro;
        this.usuarioSelecionado = this.usuarioResponsavelQuadro;
      }
    },
  },

  methods: {
    async salvarQuadro() {
      this.form.usuarioId = this.usuarioSelecionado.value;
      await QuadroService.salvarQuadro(this.form)
        .then(() => {
          this.$emit("salvar-quadro");
        })
        .catch((err) => {
          NotificationUtil.returnFeedbackMessage(
            this.$q,
            "Falha ao editar quadro",
            "negative",
            "red"
          );
          console.log(err);
        });
    },
  },
});
</script>
