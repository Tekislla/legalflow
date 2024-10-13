<template>
  <q-page class="flex flex-center">
    <div class="flex column flex-center main-text">
      <div style="font-size: 8vh">Olá, {{ userName }}!</div>
      <div class="text-h4" v-show="this.userRole === 'ADMIN'">
        Selecione um quadro no menu ou crie um novo quadro
      </div>
      <div class="text-h4" v-show="this.userRole === 'USER'">
        Selecione um quadro no menu ou solicite acesso a um quadro existente ao
        administrador
      </div>
      <div class="flex-row" v-show="this.userRole === 'ADMIN'">
        <q-btn
          class="index-btn"
          color="teal"
          v-on:click="this.$emit('abrir-modal-novo-usuario')"
        >
          <div>
            <div>Novo usuário</div>
            <q-icon name="person" size="xl" />
          </div>
        </q-btn>
        <q-btn
          class="index-btn"
          color="teal"
          v-on:click="this.$emit('abrir-modal-novo-quadro')"
        >
          <div>
            <div>Novo quadro</div>
            <q-icon name="topic" size="xl" />
          </div>
        </q-btn>
      </div>
    </div>
  </q-page>
</template>

<script>
import { defineComponent } from "vue";

export default defineComponent({
  name: "IndexPage",

  props: {
    userRole: String,
    userName: String,
  },
});
</script>
