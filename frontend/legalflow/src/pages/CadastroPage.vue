<template>
  <q-page class="flex column flex-center">
    <q-card class="form-card">
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
        />
        <br />
        <q-input
          stack-label
          outlined
          v-model="form.email"
          type="email"
          label="Email"
          required
        />
        <br />
        <q-input
          stack-label
          outlined
          v-model="form.nomeOrganizacao"
          label="Nome da Organização"
          required
        />
        <br />
        <q-input
          stack-label
          outlined
          v-model="form.documentoOrganizacao"
          label="Documento da Organização"
          required
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
            !form.senha ||
            !form.nome ||
            !form.nomeOrganizacao ||
            !form.documentoOrganizacao ||
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

export default defineComponent({
  name: "RegisterPage",

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
