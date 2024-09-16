<template>
  <q-header elevated class="bg-black text-white flex" height-hint="98">
    <q-toolbar>
      <q-btn
        dense
        flat
        round
        icon="menu"
        @click="this.$emit('toggle-left-drawer')"
      />

      <q-toolbar-title> LegalFlow </q-toolbar-title>

      <q-btn
        v-show="actualQuadroId != null && userRole === 'ADMIN'"
        v-on:click="this.$emit('abrir-modal-novo-usuario')"
        unelevated
        size="md"
        label="Novo Usuário"
        no-caps
        class="header-btn"
        color="teal"
      />
      <q-btn
        v-show="actualQuadroId != null && userRole === 'ADMIN'"
        v-on:click="this.$emit('abrir-modal-novo-quadro')"
        unelevated
        size="md"
        label="Novo Quadro"
        no-caps
        class="header-btn"
        color="teal"
      />
    </q-toolbar>

    <q-tabs align="left" v-model="tabProxy">
      <q-tab name="CRIADO" label="Criado" v-show="actualQuadroId != null">
        <q-badge color="teal">
          {{ processosCriados }}
        </q-badge>
      </q-tab>
      <q-tab
        name="EM_PROGRESSO"
        label="Em Progresso"
        v-show="actualQuadroId != null"
      >
        <q-badge color="teal">
          {{ processosEmProgresso }}
        </q-badge>
      </q-tab>
      <q-tab
        name="FINALIZADO"
        label="Finalizado"
        v-show="actualQuadroId != null"
      >
        <q-badge color="teal">
          {{ processosFinalizados }}
        </q-badge>
      </q-tab>
      <q-tab name="ARQUIVADO" label="Arquivado" v-show="actualQuadroId != null">
        <q-badge color="teal">
          {{ processosArquivados }}
        </q-badge>
      </q-tab>
    </q-tabs>
  </q-header>
</template>
<script>
import { defineComponent } from "vue";
import { useStore } from "vuex";

export default defineComponent({
  name: "HeaderComponent",

  props: {
    processosCriados: Number,
    processosEmProgresso: Number,
    processosFinalizados: Number,
    processosArquivados: Number,
    actualQuadroId: Number,
  },

  setup() {
    const store = useStore();
    const userRole = store.state.usuario.role;

    return {
      store,
      userRole,
    };
  },

  data() {
    return {
      tabProxy: "CRIADO",
    };
  },

  watch: {
    tabProxy() {
      this.$emit("update:tab", this.tabProxy);
    },
  },
});
</script>
