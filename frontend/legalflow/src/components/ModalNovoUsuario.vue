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
              (val) => val.length <= 20 || 'Máximo de 50 caracteres',
              (val) => val.length >= 5 || 'Mínimo de 5 caracteres',
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
              (val) => val.length <= 20 || 'Máximo de 20 caracteres',
              (val) => val.length >= 5 || 'Mínimo de 5 caracteres',
            ]"
          />

          <q-select
            v-model="form.administrador"
            :options="roleList"
            label="Role"
            outlined
            required
            dense
          />

          <q-card-actions align="right">
            <q-btn flat label="Cancelar" color="black" no-caps v-close-popup />
            <q-btn
              @click="cadastrar()"
              unelevated
              size="md"
              label="Cadastrar usuário"
              no-caps
              color="teal"
              :disable="
                form.nome === '' ||
                form.nome.length < 5 ||
                form.nome.length > 50 ||
                form.email === '' ||
                form.senha === '' ||
                form.senha.length < 5 ||
                form.senha.length > 20 ||
                form.role === ''
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
        administrador: null,
        organizacaoId: null,
      },
      roleList: [
        {
          label: "Administrador",
          value: true,
        },
        {
          label: "Usuário",
          value: false,
        },
      ],
    };
  },

  methods: {
    async cadastrar() {
      this.form.organizacaoId = this.store.state.organizacaoId;
      this.form.administrador = this.form.administrador.value;
      await this.store.dispatch("cadastrar", this.form);
      this.resetForm();
      this.$emit("salvar-usuario");
    },
    resetForm() {
      this.form = {
        nome: "",
        email: "",
        senha: "",
        administrador: null,
        organizacaoId: this.store.state.organizacaoId,
      };
    },
  },
});
</script>
