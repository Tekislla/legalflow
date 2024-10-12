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
            stack-label
            label="Nome"
            outlined
            required
            :rules="[
              (val) => val.length <= 20 || 'Máximo de 50 caracteres',
              (val) => val.length >= 5 || 'Mínimo de 5 caracteres',
            ]"
          />
          <q-input
            v-model="form.email"
            stack-label
            label="E-mail"
            outlined
            required
            :rules="[
              (val) => val.length <= 50 || 'Máximo de 50 caracteres',
              (val) => val.length >= 10 || 'Mínimo de 10 caracteres',
              (val) => validarEmail() || 'E-mail inválido',
            ]"
          />
          <q-input
            v-model="form.senha"
            stack-label
            label="Senha"
            type="password"
            outlined
            required
            :rules="[
              (val) => val.length <= 20 || 'Máximo de 20 caracteres',
              (val) => val.length >= 5 || 'Mínimo de 5 caracteres',
            ]"
          />

          <q-select
            v-model="form.administrador"
            stack-label
            :options="roleList"
            label="Role"
            outlined
            required
          />

          <q-card-actions align="right">
            <q-btn
              flat
              @click="resetForm()"
              label="Cancelar"
              color="black"
              no-caps
              v-close-popup
            />
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
                form.email.length < 10 ||
                form.email.length > 50 ||
                !validarEmail() ||
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
    validarEmail() {
      if (this.form.email) {
        const re = /\S+@\S+\.\S+/;
        return re.test(this.form.email);
      }
      return false;
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
