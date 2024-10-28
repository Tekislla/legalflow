<template>
  <q-page class="flex column flex-center">
    <q-card class="form-card">
      <simple-header-component />
      <q-card-section>
        <div class="text-h6">Cadastro</div>
      </q-card-section>

      <q-card-section>
        <q-input
          stack-label
          outlined
          v-model="form.nome"
          label="Nome"
          required
          :rules="[
            (val) => val.length <= 50 || 'Máximo de 50 caracteres',
            (val) => val.length >= 5 || 'Mínimo de 5 caracteres',
          ]"
        />
        <br />
        <q-input
          stack-label
          outlined
          v-model="form.email"
          type="email"
          label="Email"
          required
          :rules="[
            (val) => val.length <= 50 || 'Máximo de 50 caracteres',
            (val) => val.length >= 10 || 'Mínimo de 10 caracteres',
            (val) => validarEmail() || 'E-mail inválido',
          ]"
        />
        <br />
        <q-input
          stack-label
          outlined
          v-model="form.nomeOrganizacao"
          label="Nome da Organização"
          required
          :rules="[
            (val) => val.length <= 100 || 'Máximo de 100 caracteres',
            (val) => val.length >= 5 || 'Mínimo de 5 caracteres',
          ]"
        />
        <br />
        <q-input
          stack-label
          outlined
          v-model="form.documentoOrganizacao"
          label="Documento da Organização"
          required
          :rules="[
            (val) => val.length <= 20 || 'Máximo de 20 caracteres',
            (val) => val.length >= 11 || 'Mínimo de 11 caracteres',
          ]"
        />
        <br />
        <q-input
          stack-label
          outlined
          v-model="form.senha"
          :type="isPwd ? 'password' : 'text'"
          label="Senha"
          required
          v-on:keyup.enter="cadastrar"
          :rules="[
            (val) => val.length <= 20 || 'Máximo de 20 caracteres',
            (val) => val.length >= 5 || 'Mínimo de 5 caracteres',
          ]"
        >
          <template v-slot:append>
            <q-icon
              :name="isPwd ? 'visibility_off' : 'visibility'"
              class="cursor-pointer"
              @click="isPwd = !isPwd"
            />
          </template>
        </q-input>
      </q-card-section>

      <q-card-actions align="center">
        <q-btn
          :loading="loading"
          class="register-login-btn"
          label="Cadastrar"
          color="teal"
          @click="cadastrar"
          :disable="
            !form.email ||
            form.email.length < 10 ||
            form.email.length > 50 ||
            !form.senha ||
            form.senha.length < 5 ||
            form.senha.length > 20 ||
            !form.nome ||
            form.nome.length < 5 ||
            form.nome.length > 50 ||
            !form.nomeOrganizacao ||
            form.nomeOrganizacao.length < 5 ||
            form.nomeOrganizacao.length > 100 ||
            !form.documentoOrganizacao ||
            form.documentoOrganizacao.length < 11 ||
            form.documentoOrganizacao.length > 20 ||
            !validarEmail()
          "
        />
      </q-card-actions>
    </q-card>
    <p class="register-login-text">
      Já tem uma conta?
      <a class="register-login-text-action" @click="redirectLogin()"
        >Faça login.</a
      >
    </p>
  </q-page>
</template>

<script>
import { defineComponent } from "vue";
import NotificationUtil from "@/utils/NotificationUtil";
import SimpleHeaderComponent from "@/components/SimpleHeaderComponent.vue";

export default defineComponent({
  name: "RegisterPage",

  components: {
    SimpleHeaderComponent,
  },

  data() {
    return {
      form: {
        nome: null,
        email: null,
        senha: null,
        nomeOrganizacao: null,
        documentoOrganizacao: null,
        administrador: true,
      },
      isPwd: true,
      loading: false,
    };
  },

  methods: {
    redirectLogin() {
      this.$router.push({ path: "/auth/login" });
    },
    validarEmail() {
      if (this.form.email) {
        const re = /\S+@\S+\.\S+/;
        return re.test(this.form.email);
      }
      return false;
    },
    async cadastrar() {
      this.loading = true;
      try {
        await this.$store.dispatch("cadastrar", this.form);

        await this.$store.dispatch("login", {
          email: this.form.email,
          senha: this.form.senha,
        });

        this.loading = false;

        if (this.$store.state.token) {
          this.$router.push({ path: "/dashboard" });
        } else {
          NotificationUtil.returnFeedbackMessage(
            this.$q,
            "Erro ao logar após o cadastro",
            "negative",
            "red"
          );
        }
      } catch (error) {
        this.loading = false;
        NotificationUtil.returnFeedbackMessage(
          this.$q,
          error.response?.data || "",
          "negative",
          "red"
        );
      }
    },
  },
});
</script>
