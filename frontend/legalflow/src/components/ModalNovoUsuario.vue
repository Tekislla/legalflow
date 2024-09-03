<template>
  <q-dialog>
    <q-card class="q-pa-md main-card">
      <q-card-section class="row items-center q-pb-none">
        <div class="text-h6">Novo Usuário</div>
        <q-space />
      </q-card-section>

      <q-card-section>
        <div class="q-gutter-md">
          <q-input
            v-model="form.nome"
            label="Name"
            outlined
            required
            dense
            :rules="[
              (val) => val.length <= 100 || 'Please use maximum 100 characters',
            ]"
          />
          <q-input
            v-model="form.email"
            label="E-mail"
            outlined
            required
            dense
            :rules="[
              (val) => val.length <= 100 || 'Please use maximum 100 characters',
            ]"
          />
          <q-input
            v-model="form.senha"
            label="Senha"
            type="password"
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
              @click="cadastrar()"
              unelevated
              size="md"
              label="Create customer"
              no-caps
              color="teal"
              :disable="
                form.nome === '' || form.email === '' || form.senha === ''
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
import { useStore } from "vuex";

export default defineComponent({
  name: "ModalNovoUsuario",

  setup() {
    const store = useStore();

    return {
      store,
    };
  },

  data() {
    return {
      form: {
        nome: "",
        email: "",
        senha: "",
        administrador: false,
        organizacaoId: null,
      },
    };
  },

  methods: {
    cadastrar() {
      console.log(this.store);
      this.form.organizacaoId = this.store.state.usuario.organizacaoId;
      this.store.dispatch("cadastrar", this.form);
      this.resetForm();
      this.$emit("submit-form-novo-usuario");
    },
    resetForm() {
      this.form = {
        nome: "",
        email: "",
        senha: "",
        administrador: false,
        organizacaoId: this.store.state.usuario.organizacaoId,
      };
    },
  },
});
</script>
