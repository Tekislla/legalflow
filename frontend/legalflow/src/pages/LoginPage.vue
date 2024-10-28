<template>
  <q-page class="flex column flex-center">
    <q-card class="form-card">
      <simple-header-component />
      <q-card-section>
        <div class="text-h6">Login</div>
      </q-card-section>

      <q-card-section>
        <q-input
          stack-label
          outlined
          required
          v-model="email"
          label="E-mail"
          type="email"
          v-on:keyup.enter="login"
          :rules="[(val) => validarEmail() || 'Digite um e-mail válido']"
        />
        <br />
        <q-input
          stack-label
          outlined
          v-model="senha"
          label="Senha"
          :type="isPwd ? 'password' : 'text'"
          v-on:keyup.enter="login"
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
          label="Entrar"
          color="teal"
          @click="login"
          :disable="loading || !email || !senha || !validarEmail()"
        />
      </q-card-actions>
    </q-card>
    <p class="register-login-text">
      Ainda não tem uma conta?
      <a class="register-login-text-action" @click="redirectCadastro()"
        >Cadastre-se.</a
      >
    </p>
  </q-page>
</template>

<script>
import { defineComponent } from "vue";
import NotificationUtil from "@/utils/NotificationUtil";
import SimpleHeaderComponent from "@/components/SimpleHeaderComponent.vue";

export default defineComponent({
  name: "LoginPage",

  components: {
    SimpleHeaderComponent,
  },

  data() {
    return {
      loading: false,
      email: null,
      senha: null,
      isPwd: true,
    };
  },

  methods: {
    redirectCadastro() {
      this.$router.push({ path: "/auth/cadastro" });
    },
    validarEmail() {
      if (this.email) {
        const re = /\S+@\S+\.\S+/;
        return re.test(this.email);
      }
      return false;
    },
    async login() {
      this.loading = true;
      try {
        await this.$store.dispatch("login", {
          email: this.email,
          senha: this.senha,
        });

        this.loading = false;
        if (this.$store.state.token) {
          this.$router.push({ path: "/dashboard" });
        } else {
          NotificationUtil.returnFeedbackMessage(
            this.$q,
            "Falha no login",
            "negative",
            "red"
          );
        }
      } catch (err) {
        this.loading = false;
        NotificationUtil.returnFeedbackMessage(
          this.$q,
          err.response?.data || "Falha no login",
          "negative",
          "red"
        );
      }
    },
  },
});
</script>
