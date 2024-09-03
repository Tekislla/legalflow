<template>
  <q-page class="flex flex-center">
    <q-card class="q-mb-md" style="max-width: 400px; width: 100%">
      <q-card-section>
        <div class="text-h6">Cadastro</div>
      </q-card-section>

      <q-card-section>
        <q-form @submit.prevent="handleSubmit" class="q-gutter-md">
          <q-input v-model="form.nome" label="Nome" required />
          <q-input v-model="form.email" type="email" label="Email" required />
          <q-input
            v-model="form.senha"
            type="password"
            label="Senha"
            required
          />
          <q-input
            v-model="form.nomeOrganizacao"
            label="Nome da Organização"
            required
          />
          <q-input
            v-model="form.documentoOrganizacao"
            label="Documento da Organização"
            required
          />

          <q-btn type="submit" label="Cadastrar" color="primary" />
        </q-form>
      </q-card-section>
    </q-card>
  </q-page>
</template>

<script>
import { ref } from "vue";
import { useStore } from "vuex";

export default {
  name: "RegisterPage",

  setup() {
    const form = ref({
      nome: "",
      email: "",
      senha: "",
      nomeOrganizacao: "",
      documentoOrganizacao: "",
      role: "ADMIN",
    });

    const $store = useStore();

    const handleSubmit = async () => {
      console.log($store);
      try {
        $store.dispatch("cadastrar", form.value);
        console.log("Cadastro realizado com sucesso!");
      } catch (error) {
        console.error("Erro ao cadastrar:", error);
      }
    };

    return {
      form,
      handleSubmit,
    };
  },
};
</script>

<style scoped>
.q-page {
  max-width: 100%;
}
</style>
