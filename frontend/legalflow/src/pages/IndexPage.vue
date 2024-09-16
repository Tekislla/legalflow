<template>
  <q-page class="flex flex-center">
    <div class="flex column flex-center main-text">
      <div style="font-size: 8vh">Olá!</div>
      <div class="text-h4">
        Selecione um quadro no menu ou crie um novo quadro
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
import { useStore } from "vuex";

export default defineComponent({
  name: "IndexPage",

  setup() {
    const store = useStore();
    const userRole = store.state.usuario.role;
    console.log(store.state);

    return {
      store,
      userRole,
    };
  },
});
</script>
