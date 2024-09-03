<template>
  <q-page class="flex flex-center">
    <q-card>
      <q-card-section>
        <div class="text-h6">Login</div>
      </q-card-section>

      <q-card-section>
        <q-input v-model="email" label="Email" type="email" />
        <q-input v-model="senha" label="Senha" type="password" />
      </q-card-section>

      <q-card-actions align="right">
        <q-btn label="Entrar" color="primary" @click="login" />
      </q-card-actions>
    </q-card>
  </q-page>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { useStore } from "vuex";

const email = ref("");
const senha = ref("");
const router = useRouter();
const $store = useStore();

const login = async () => {
  if (email.value && senha.value) {
    try {
      console.log("Base URL:", process.env.VUE_APP_API_BASE_URL);

      await $store.dispatch("login", {
        email: email.value,
        senha: senha.value,
      });

      // Verifica se o token foi definido e realiza o redirecionamento
      if ($store.state.token) {
        router.push({ path: "/" });
      } else {
        console.error("Falha no login");
      }
    } catch (error) {
      console.error("Erro ao fazer login:", error);
    }
  } else {
    console.error("Email e senha são obrigatórios");
  }
};
</script>
